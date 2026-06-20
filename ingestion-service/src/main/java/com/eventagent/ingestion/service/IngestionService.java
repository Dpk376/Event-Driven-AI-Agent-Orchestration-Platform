package com.eventagent.ingestion.service;

import com.eventagent.common.event.ClinicalEvent;
import com.eventagent.common.kafka.KafkaTopics;
import com.eventagent.common.tracing.TracingConstants;
import com.eventagent.ingestion.dto.ClinicalEventRequest;
import com.eventagent.ingestion.entity.ClinicalEventEntity;
import com.eventagent.ingestion.outbox.OutboxEntry;
import com.eventagent.ingestion.outbox.OutboxRepository;
import com.eventagent.ingestion.repository.ClinicalEventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class IngestionService {

    private static final Logger log = LoggerFactory.getLogger(IngestionService.class);

    private final ClinicalEventRepository eventRepository;
    private final OutboxRepository outboxRepository;

    public IngestionService(ClinicalEventRepository eventRepository, OutboxRepository outboxRepository) {
        this.eventRepository = eventRepository;
        this.outboxRepository = outboxRepository;
    }

    /**
     * Processes an incoming event request. Ensures idempotency based on the idempotency key.
     * If the event is new, persists it to the database along with an outbox entry in a single transaction.
     *
     * @param request The incoming event request.
     * @return the generated eventId if new, or the existing eventId if duplicate.
     */
    @Transactional
    public String ingestEvent(ClinicalEventRequest request) {
        Optional<ClinicalEventEntity> existing = eventRepository.findByIdempotencyKey(request.idempotencyKey());
        if (existing.isPresent()) {
            log.info("Duplicate event detected for idempotencyKey: {}", request.idempotencyKey());
            return existing.get().getEventId();
        }

        String eventId = UUID.randomUUID().toString();
        Instant now = Instant.now();

        ClinicalEventEntity entity = new ClinicalEventEntity(
                UUID.randomUUID(),
                eventId,
                request.patientId(),
                request.eventType(),
                request.payload(),
                request.idempotencyKey(),
                now
        );
        eventRepository.save(entity);

        String correlationId = MDC.get(TracingConstants.MDC_CORRELATION_ID);
        String traceId = MDC.get(TracingConstants.MDC_TRACE_ID);

        ClinicalEvent clinicalEvent = new ClinicalEvent(
                eventId,
                request.patientId(),
                request.eventType(),
                request.payload(),
                request.idempotencyKey(),
                now,
                correlationId,
                traceId
        );

        OutboxEntry outboxEntry = new OutboxEntry(
                UUID.randomUUID(),
                eventId,
                KafkaTopics.CLINICAL_EVENTS_RAW,
                clinicalEvent
        );
        outboxRepository.save(outboxEntry);

        log.info("Successfully ingested eventId: {}", eventId);
        return eventId;
    }
}
