package com.eventagent.orchestrator.service;

import com.eventagent.common.event.AgentDecisionEvent;
import com.eventagent.common.event.ClinicalEvent;
import com.eventagent.common.event.RoutingDecision;
import com.eventagent.common.event.Severity;
import com.eventagent.orchestrator.llm.LlmClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class AgentStateMachine {

    private static final Logger log = LoggerFactory.getLogger(AgentStateMachine.class);
    private static final String AGENT_NAME = "ClinicalTriageAgent_v1";

    private final LlmClient llmClient;

    public AgentStateMachine(LlmClient llmClient) {
        this.llmClient = llmClient;
    }

    /**
     * Orchestrates the evaluation of a clinical event through the AI agent pipeline:
     * CLASSIFYING -> SUMMARIZING -> ROUTING -> ACTING.
     *
     * @param event the raw clinical event
     * @return the final agent decision event
     */
    public AgentDecisionEvent evaluateEvent(ClinicalEvent event) {
        log.info("Starting evaluation for event {} (CorrelationId: {})", event.eventId(), event.correlationId());

        long totalLatencyMs = 0;
        int totalInputTokens = 0;
        int totalOutputTokens = 0;
        long totalCostMicros = 0;
        String currentStep = "CLASSIFYING";

        try {
            // Step 1: CLASSIFYING
            log.debug("Step: {}", currentStep);
            var severityResponse = llmClient.classifySeverity(event);
            totalLatencyMs += severityResponse.latencyMs();
            totalInputTokens += severityResponse.inputTokens();
            totalOutputTokens += severityResponse.outputTokens();
            totalCostMicros += severityResponse.costMicros();
            Severity severity = severityResponse.result();

            // Step 2: SUMMARIZING
            currentStep = "SUMMARIZING";
            log.debug("Step: {}", currentStep);
            var summaryResponse = llmClient.summarize(event, severity);
            totalLatencyMs += summaryResponse.latencyMs();
            totalInputTokens += summaryResponse.inputTokens();
            totalOutputTokens += summaryResponse.outputTokens();
            totalCostMicros += summaryResponse.costMicros();
            String summary = summaryResponse.result();

            // Step 3: ROUTING
            currentStep = "ROUTING";
            log.debug("Step: {}", currentStep);
            var routingResponse = llmClient.determineRouting(event, severity, summary);
            totalLatencyMs += routingResponse.latencyMs();
            totalInputTokens += routingResponse.inputTokens();
            totalOutputTokens += routingResponse.outputTokens();
            totalCostMicros += routingResponse.costMicros();
            RoutingDecision decision = routingResponse.result();

            // Step 4: ACTING (Assemble final result)
            currentStep = "ACTING";
            log.debug("Step: {}", currentStep);
            
            String reason = String.format("Agent decided to %s based on %s severity.", decision, severity);

            AgentDecisionEvent decisionEvent = new AgentDecisionEvent(
                    UUID.randomUUID().toString(),
                    event.eventId(),
                    event.patientId(),
                    severity,
                    summary,
                    decision,
                    reason,
                    AGENT_NAME,
                    currentStep, // Final step
                    totalInputTokens,
                    totalOutputTokens,
                    totalCostMicros,
                    totalLatencyMs,
                    event.traceId(),
                    event.correlationId(),
                    Instant.now()
            );

            log.info("Successfully evaluated event {}. Decision: {}", event.eventId(), decision);
            return decisionEvent;

        } catch (Exception e) {
            log.error("Failed during step {} for event {}", currentStep, event.eventId(), e);
            throw new RuntimeException("Agent pipeline failed at step: " + currentStep, e);
        }
    }
}
