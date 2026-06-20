package com.eventagent.common.metrics;

/**
 * Central registry of all Micrometer metric names and tag keys used
 * across the Event-Driven AI Agent Orchestration Platform.
 *
 * <p>All services must reference these constants when recording metrics
 * to ensure consistent naming in dashboards, alerts, and Prometheus queries.
 * Metric names follow the convention {@code domain.entity.unit} with dots
 * as separators.</p>
 *
 * <p>Tag keys follow the convention {@code snake_case} for compatibility
 * with Prometheus and Grafana label naming standards.</p>
 *
 * <p>This class is non-instantiable by design.</p>
 */
public final class MetricNames {

    // --- Counter Metrics ---

    /** Total count of clinical events ingested from upstream sources. */
    public static final String EVENTS_INGESTED_TOTAL = "events.ingested.total";

    /** Total count of clinical events successfully processed by the pipeline. */
    public static final String EVENTS_PROCESSED_TOTAL = "events.processed.total";

    /** Total count of events dropped due to idempotency deduplication. */
    public static final String EVENTS_DEDUPLICATED_TOTAL = "events.deduplicated.total";

    /** Total count of messages sent to Dead Letter Topics. */
    public static final String DLQ_MESSAGES_TOTAL = "dlq.messages.total";

    /** Total count of notifications successfully sent to recipients. */
    public static final String NOTIFICATIONS_SENT_TOTAL = "notifications.sent.total";

    /** Total count of pipeline processing errors. */
    public static final String PIPELINE_ERRORS_TOTAL = "pipeline.errors.total";

    /** Total LLM tokens consumed (input + output), tagged by direction. */
    public static final String LLM_TOKENS_TOTAL = "llm.tokens.total";

    /** Total LLM cost in microdollars (1 microdollar = 1/1,000,000 USD). */
    public static final String LLM_COST_MICROS_TOTAL = "llm.cost.micros.total";

    // --- Timer / Distribution Metrics ---

    /** Duration of individual agent pipeline steps in seconds. */
    public static final String AGENT_STEP_DURATION_SECONDS = "agent.step.duration.seconds";

    /** Duration of LLM API calls in seconds. */
    public static final String LLM_CALL_DURATION_SECONDS = "llm.call.duration.seconds";

    // --- Gauge Metrics ---

    /** Current state of the circuit breaker (0=closed, 1=open, 2=half-open). */
    public static final String CIRCUIT_BREAKER_STATE = "circuit.breaker.state";

    /** Current Kafka consumer lag (messages behind head of partition). */
    public static final String KAFKA_CONSUMER_LAG = "kafka.consumer.lag";

    /** Remaining token budget for the current billing period. */
    public static final String TOKEN_BUDGET_REMAINING = "token.budget.remaining";

    /** Count of outbox messages pending publication to Kafka. */
    public static final String OUTBOX_PENDING_TOTAL = "outbox.pending.total";

    // --- Tag Keys ---

    /** Tag key for the clinical event type (e.g., LAB_RESULT, VITAL_SIGN). */
    public static final String TAG_EVENT_TYPE = "event_type";

    /** Tag key for the name of the AI agent that processed the event. */
    public static final String TAG_AGENT_NAME = "agent_name";

    /** Tag key for the pipeline step name (e.g., triage, enrichment, decision). */
    public static final String TAG_PIPELINE_STEP = "pipeline_step";

    /** Tag key for the severity level of a decision or event. */
    public static final String TAG_SEVERITY = "severity";

    /** Tag key for the routing decision outcome. */
    public static final String TAG_DECISION = "decision";

    /** Tag key for the processing status (e.g., success, failure, timeout). */
    public static final String TAG_STATUS = "status";

    /** Tag key for the Kafka topic name. */
    public static final String TAG_TOPIC = "topic";

    /** Tag key for the exception class name. */
    public static final String TAG_EXCEPTION = "exception";

    private MetricNames() {
        throw new UnsupportedOperationException(
                "MetricNames is a constants class and cannot be instantiated");
    }
}
