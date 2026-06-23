package com.eventagent.orchestrator.messaging;

import com.eventagent.common.event.AgentDecisionEvent;
import com.eventagent.common.event.ClinicalEvent;
import com.eventagent.common.event.TriagedEvent;
import com.eventagent.common.kafka.KafkaTopics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class AgentDecisionProducer {

    private static final Logger log = LoggerFactory.getLogger(AgentDecisionProducer.class);

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public AgentDecisionProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishDecision(ClinicalEvent originalEvent, AgentDecisionEvent decisionEvent) {
        log.debug("Publishing AgentDecisionEvent {} to topic {}", decisionEvent.decisionId(), KafkaTopics.AGENT_DECISIONS);
        kafkaTemplate.send(KafkaTopics.AGENT_DECISIONS, decisionEvent.eventId(), decisionEvent);
        
        // As discussed, also publish TriagedEvent to clinical.events.triaged
        TriagedEvent triagedEvent = new TriagedEvent(
                decisionEvent.eventId(),
                decisionEvent.patientId(),
                originalEvent.eventType(),
                decisionEvent.severity(),
                decisionEvent.decision(),
                decisionEvent.summary(),
                decisionEvent.reason(),
                decisionEvent.decisionId(),
                decisionEvent.correlationId(),
                decisionEvent.traceId(),
                decisionEvent.timestamp()
        );
        log.debug("Publishing TriagedEvent for {} to topic {}", decisionEvent.eventId(), KafkaTopics.CLINICAL_EVENTS_TRIAGED);
        kafkaTemplate.send(KafkaTopics.CLINICAL_EVENTS_TRIAGED, decisionEvent.eventId(), triagedEvent);
    }
}
