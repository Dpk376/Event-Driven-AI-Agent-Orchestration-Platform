package com.eventagent.orchestrator.llm;

import com.eventagent.common.event.ClinicalEvent;
import com.eventagent.common.event.RoutingDecision;
import com.eventagent.common.event.Severity;
import org.springframework.stereotype.Component;

@Component
public class FakeLlmClient implements LlmClient {

    @Override
    public LlmResponse<Severity> classifySeverity(ClinicalEvent event) {
        // Deterministic mock logic based on event type
        Severity severity;
        switch (event.eventType()) {
            case LAB_RESULT -> severity = Severity.MEDIUM;
            case VITAL_SIGN -> severity = Severity.HIGH;
            case ADMISSION -> severity = Severity.LOW;
            default -> severity = Severity.LOW;
        }
        return new LlmResponse<>(severity, 50, 10, 100, 150);
    }

    @Override
    public LlmResponse<String> summarize(ClinicalEvent event, Severity severity) {
        String summary = String.format("Patient %s had a %s severity event of type %s.",
                event.patientId(), severity, event.eventType());
        return new LlmResponse<>(summary, 60, 30, 200, 250);
    }

    @Override
    public LlmResponse<RoutingDecision> determineRouting(ClinicalEvent event, Severity severity, String summary) {
        RoutingDecision decision;
        if (severity == Severity.HIGH || severity == Severity.CRITICAL) {
            decision = RoutingDecision.ESCALATE;
        } else if (severity == Severity.MEDIUM) {
            decision = RoutingDecision.NOTIFY;
        } else {
            decision = RoutingDecision.NO_ACTION;
        }
        return new LlmResponse<>(decision, 100, 15, 150, 200);
    }
}
