package com.eventagent.common.kafka;

/**
 * Central registry of all Kafka topic names and partition configurations
 * used by the Event-Driven AI Agent Orchestration Platform.
 *
 * <p>All services must reference these constants rather than hardcoding
 * topic names. This ensures consistent topic naming across producers,
 * consumers, and infrastructure provisioning scripts.</p>
 *
 * <p>Dead Letter Topics (DLT) follow the convention of appending ".DLT"
 * to the source topic name.</p>
 *
 * <p>This class is non-instantiable by design.</p>
 */
public final class KafkaTopics {

    // --- Primary Topics ---

    /** Raw clinical events ingested from upstream systems (HL7, FHIR, device feeds). */
    public static final String CLINICAL_EVENTS_RAW = "clinical.events.raw";

    /** Clinical events that have been triaged by the AI agent pipeline. */
    public static final String CLINICAL_EVENTS_TRIAGED = "clinical.events.triaged";

    /** Decisions produced by AI agents after evaluating clinical events. */
    public static final String AGENT_DECISIONS = "agent.decisions";

    /** Outbound notifications destined for care teams, patients, or escalation channels. */
    public static final String NOTIFICATIONS_OUTBOUND = "notifications.outbound";

    // --- Dead Letter Topics ---

    /** DLT for failed raw clinical event processing. */
    public static final String CLINICAL_EVENTS_RAW_DLT = "clinical.events.raw.DLT";

    /** DLT for failed triaged clinical event processing. */
    public static final String CLINICAL_EVENTS_TRIAGED_DLT = "clinical.events.triaged.DLT";

    /** DLT for failed agent decision processing. */
    public static final String AGENT_DECISIONS_DLT = "agent.decisions.DLT";

    // --- Partition Counts ---

    /** Partition count for the raw clinical events topic. */
    public static final int RAW_PARTITION_COUNT = 6;

    /** Partition count for the triaged clinical events topic. */
    public static final int TRIAGED_PARTITION_COUNT = 6;

    /** Partition count for the agent decisions topic. */
    public static final int DECISIONS_PARTITION_COUNT = 6;

    /** Partition count for the notifications outbound topic. */
    public static final int NOTIFICATIONS_PARTITION_COUNT = 3;

    private KafkaTopics() {
        throw new UnsupportedOperationException("KafkaTopics is a constants class and cannot be instantiated");
    }
}
