package com.eventagent.ingestion.outbox;

import com.eventagent.common.event.ClinicalEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
public class OutboxRelay {

    private static final Logger log = LoggerFactory.getLogger(OutboxRelay.class);

    private final OutboxRepository outboxRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public OutboxRelay(OutboxRepository outboxRepository, KafkaTemplate<String, Object> kafkaTemplate) {
        this.outboxRepository = outboxRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Scheduled(fixedDelay = 1000)
    public void relayEvents() {
        List<OutboxEntry> pendingEvents = outboxRepository.findByStatusOrderByCreatedAtAsc(OutboxEntry.Status.PENDING);
        if (pendingEvents.isEmpty()) {
            return;
        }

        log.debug("Found {} pending outbox events", pendingEvents.size());

        for (OutboxEntry entry : pendingEvents) {
            ClinicalEvent eventPayload = (ClinicalEvent) entry.getPayload();
            String key = eventPayload.patientId(); // Partition key

            CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(entry.getTopic(), key, eventPayload);
            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    entry.markPublished();
                    outboxRepository.save(entry);
                    log.debug("Successfully published event {} to Kafka", entry.getEventId());
                } else {
                    log.error("Failed to publish event {} to Kafka: {}", entry.getEventId(), ex.getMessage());
                }
            });
        }
    }
}
