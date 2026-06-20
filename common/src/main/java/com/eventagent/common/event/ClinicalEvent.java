package com.eventagent.common.event;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

/**
 * Core event contract representing a clinical event flowing through the platform.
 *
 * <p>This record is the canonical schema for raw clinical events ingested from
 * hospital information systems, EHR platforms, and medical device feeds. It is
 * serialized to JSON and published to the {@code clinical.events.raw} Kafka topic.</p>
 *
 * <p>Key design decisions:</p>
 * <ul>
 *   <li><b>Idempotency</b> - Each event carries an {@code idempotencyKey} to enable
 *       deduplication at every consumer. Consumers should check this key against a
 *       persistent store before processing.</li>
 *   <li><b>Correlation</b> - The {@code correlationId} ties together all events,
 *       decisions, and notifications that originate from a single clinical trigger.</li>
 *   <li><b>Tracing</b> - The {@code traceId} supports distributed tracing via
 *       OpenTelemetry or similar frameworks.</li>
 *   <li><b>Flexible payload</b> - The {@code payload} map allows varied clinical data
 *       structures (lab values, vital readings, admission details) without requiring
 *       separate event types per clinical domain.</li>
 * </ul>
 *
 * @param eventId        unique identifier for this event (UUID format)
 * @param patientId      identifier of the patient associated with this event
 * @param eventType      the category of clinical event
 * @param payload        clinical data as key-value pairs (e.g., "glucose" to 250)
 * @param idempotencyKey unique key for consumer-side deduplication (UUID format)
 * @param timestamp      when the event was created
 * @param correlationId  ID linking related events across the pipeline, nullable for raw events
 * @param traceId        distributed trace ID for observability, nullable for raw events
 */
public record ClinicalEvent(
        String eventId,
        String patientId,
        ClinicalEventType eventType,
        Map<String, Object> payload,
        String idempotencyKey,
        Instant timestamp,
        String correlationId,
        String traceId
) {

    /**
     * Creates a new {@code ClinicalEvent} with auto-generated identifiers and timestamp.
     *
     * <p>Generates a UUID for both {@code eventId} and {@code idempotencyKey},
     * sets the timestamp to the current instant, and leaves {@code correlationId}
     * and {@code traceId} as null (to be assigned by the ingestion service).</p>
     *
     * @param patientId the patient identifier, must not be null
     * @param eventType the clinical event type, must not be null
     * @param payload   the clinical data payload, must not be null
     * @return a new ClinicalEvent with generated identifiers
     */
    public static ClinicalEvent create(String patientId,
                                       ClinicalEventType eventType,
                                       Map<String, Object> payload) {
        return new ClinicalEvent(
                UUID.randomUUID().toString(),
                patientId,
                eventType,
                payload,
                UUID.randomUUID().toString(),
                Instant.now(),
                null,
                null
        );
    }
}
