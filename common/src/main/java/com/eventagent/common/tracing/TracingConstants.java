package com.eventagent.common.tracing;

/**
 * Constants for distributed tracing and structured logging across all services.
 *
 * <p>MDC (Mapped Diagnostic Context) keys are used with SLF4J/Logback to
 * attach tracing identifiers to every log statement. HTTP header names are
 * used for propagating correlation IDs across service boundaries via REST calls.</p>
 *
 * <p>This class is non-instantiable by design.</p>
 */
public final class TracingConstants {

    // --- MDC Keys ---

    /** MDC key for the correlation ID that ties together all events in a single flow. */
    public static final String MDC_CORRELATION_ID = "correlationId";

    /** MDC key for the distributed trace ID (e.g., OpenTelemetry trace ID). */
    public static final String MDC_TRACE_ID = "traceId";

    /** MDC key for the span ID within a distributed trace. */
    public static final String MDC_SPAN_ID = "spanId";

    /** MDC key for the clinical event ID being processed. */
    public static final String MDC_EVENT_ID = "eventId";

    /** MDC key for the patient ID associated with the current operation. */
    public static final String MDC_PATIENT_ID = "patientId";

    // --- HTTP Headers ---

    /** HTTP header name for propagating the correlation ID between services. */
    public static final String HEADER_CORRELATION_ID = "X-Correlation-ID";

    private TracingConstants() {
        throw new UnsupportedOperationException(
                "TracingConstants is a constants class and cannot be instantiated");
    }
}
