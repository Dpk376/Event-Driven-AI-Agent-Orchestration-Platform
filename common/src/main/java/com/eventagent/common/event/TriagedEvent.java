package com.eventagent.common.event;

import java.time.Instant;

/**
 * Represents a clinical event that has been triaged by the AI agent pipeline.
 *
 * <p>After an AI agent evaluates a raw {@link ClinicalEvent}, it produces a
 * {@code TriagedEvent} that enriches the original event with a severity
 * assessment, routing decision, and a human-readable summary. This record
 * is published to the {@code clinical.events.triaged} Kafka topic.</p>
 *
 * <p>Downstream consumers (notification service, dashboard, audit log) use
 * this record to act on the agent's assessment without needing to re-evaluate
 * the raw clinical data.</p>
 *
 * @param eventId       the ID of the original clinical event
 * @param patientId     the patient associated with this event
 * @param eventType     the category of the original clinical event
 * @param severity      the severity level assigned by the triaging agent
 * @param decision      the routing decision made by the triaging agent
 * @param summary       plain-language summary of the clinical finding
 * @param reason        explanation for the severity and routing decision
 * @param decisionId    the ID of the agent decision that produced this triage
 * @param correlationId correlation ID linking this event to the originating flow
 * @param traceId       distributed trace ID for end-to-end observability
 * @param timestamp     when this triaged event was created
 */
public record TriagedEvent(
        String eventId,
        String patientId,
        ClinicalEventType eventType,
        Severity severity,
        RoutingDecision decision,
        String summary,
        String reason,
        String decisionId,
        String correlationId,
        String traceId,
        Instant timestamp
) {
}
