package com.eventagent.common.kafka;

/**
 * Standard Kafka record header keys used across all producers and consumers
 * in the Event-Driven AI Agent Orchestration Platform.
 *
 * <p>These headers enable distributed tracing, idempotent processing, and
 * event-type-based routing without deserializing the message body.</p>
 *
 * <p>This class is non-instantiable by design.</p>
 */
public final class KafkaHeaderConstants {

    /** Header key for the correlation ID that ties related events across services. */
    public static final String CORRELATION_ID = "correlationId";

    /** Header key for the W3C Trace Context traceparent propagation header. */
    public static final String TRACE_PARENT = "traceparent";

    /** Header key for the idempotency key used for deduplication. */
    public static final String IDEMPOTENCY_KEY = "idempotencyKey";

    /** Header key indicating the type of clinical event in the record. */
    public static final String EVENT_TYPE = "eventType";

    private KafkaHeaderConstants() {
        throw new UnsupportedOperationException(
                "KafkaHeaderConstants is a constants class and cannot be instantiated");
    }
}
