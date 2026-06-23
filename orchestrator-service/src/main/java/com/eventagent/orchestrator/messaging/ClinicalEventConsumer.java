package com.eventagent.orchestrator.messaging;

import com.eventagent.common.event.AgentDecisionEvent;
import com.eventagent.common.event.ClinicalEvent;
import com.eventagent.common.kafka.KafkaTopics;
import com.eventagent.orchestrator.domain.AgentDecisionEntity;
import com.eventagent.orchestrator.repository.AgentDecisionRepository;
import com.eventagent.orchestrator.service.AgentStateMachine;
import com.eventagent.orchestrator.service.EventDeduplicator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ClinicalEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(ClinicalEventConsumer.class);

    private final EventDeduplicator deduplicator;
    private final AgentStateMachine stateMachine;
    private final AgentDecisionRepository repository;
    private final AgentDecisionProducer producer;

    public ClinicalEventConsumer(EventDeduplicator deduplicator,
                                 AgentStateMachine stateMachine,
                                 AgentDecisionRepository repository,
                                 AgentDecisionProducer producer) {
        this.deduplicator = deduplicator;
        this.stateMachine = stateMachine;
        this.repository = repository;
        this.producer = producer;
    }

    @KafkaListener(topics = KafkaTopics.CLINICAL_EVENTS_RAW, groupId = "orchestrator-group")
    @Transactional
    public void consume(ClinicalEvent event, Acknowledgment acknowledgment) {
        log.info("Received ClinicalEvent: {}", event.eventId());

        try {
            // 1. Deduplication Check
            if (!deduplicator.isNewEvent(event.idempotencyKey())) {
                log.info("Event {} is a duplicate. Skipping processing.", event.eventId());
                acknowledgment.acknowledge();
                return;
            }

            // 2. State Machine Execution
            AgentDecisionEvent decisionEvent = stateMachine.evaluateEvent(event);

            // 3. Persist to Database
            AgentDecisionEntity entity = mapToEntity(decisionEvent);
            repository.save(entity);

            // 4. Publish to Downstream Topics
            producer.publishDecision(event, decisionEvent);

            // 5. Acknowledge offset manually
            acknowledgment.acknowledge();
            log.info("Successfully processed and acknowledged ClinicalEvent: {}", event.eventId());

        } catch (Exception e) {
            log.error("Error processing ClinicalEvent {}: {}", event.eventId(), e.getMessage(), e);
            // Rethrowing so the DefaultErrorHandler handles retries and eventual DLQ routing
            throw e;
        }
    }

    private AgentDecisionEntity mapToEntity(AgentDecisionEvent event) {
        AgentDecisionEntity entity = new AgentDecisionEntity();
        entity.setDecisionId(event.decisionId());
        entity.setEventId(event.eventId());
        entity.setPatientId(event.patientId());
        entity.setSeverity(event.severity());
        entity.setSummary(event.summary());
        entity.setDecision(event.decision());
        entity.setReason(event.reason());
        entity.setAgentName(event.agentName());
        entity.setPipelineStep(event.pipelineStep());
        entity.setTokensInput(event.tokensInput());
        entity.setTokensOutput(event.tokensOutput());
        entity.setCostMicros(event.costMicros());
        entity.setLatencyMs(event.latencyMs());
        entity.setTraceId(event.traceId());
        entity.setCorrelationId(event.correlationId());
        entity.setTimestamp(event.timestamp());
        return entity;
    }
}
