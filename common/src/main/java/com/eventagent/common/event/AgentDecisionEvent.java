package com.eventagent.common.event;

import java.time.Instant;

/**
 * Records the decision made by an AI agent after evaluating a clinical event.
 *
 * <p>This record captures the full context of an agent's decision, including
 * the clinical assessment (severity, routing decision, summary) and the
 * operational telemetry (token usage, cost, latency). It is published to the
 * {@code agent.decisions} Kafka topic for downstream consumption by the
 * notification service and analytics pipelines.</p>
 *
 * <p>Cost tracking uses microdollars (1 USD = 1,000,000 microdollars) to avoid
 * floating-point precision issues in financial calculations.</p>
 *
 * @param decisionId    unique identifier for this decision (UUID format)
 * @param eventId       the ID of the clinical event that triggered this decision
 * @param patientId     the patient associated with the source event
 * @param severity      assessed severity of the clinical event
 * @param summary       plain-language summary of the clinical finding
 * @param decision      the routing action determined by the agent
 * @param reason        explanation for why this decision was made
 * @param agentName     name of the AI agent that produced this decision
 * @param pipelineStep  the pipeline step during which this decision was made
 * @param tokensInput   number of input tokens consumed by the LLM call
 * @param tokensOutput  number of output tokens produced by the LLM call
 * @param costMicros    cost of the LLM call in microdollars
 * @param latencyMs     wall-clock latency of the LLM call in milliseconds
 * @param traceId       distributed trace ID for end-to-end observability
 * @param correlationId correlation ID linking this decision to the originating event flow
 * @param timestamp     when this decision was created
 */
public record AgentDecisionEvent(
        String decisionId,
        String eventId,
        String patientId,
        Severity severity,
        String summary,
        RoutingDecision decision,
        String reason,
        String agentName,
        String pipelineStep,
        int tokensInput,
        int tokensOutput,
        long costMicros,
        long latencyMs,
        String traceId,
        String correlationId,
        Instant timestamp
) {
}
