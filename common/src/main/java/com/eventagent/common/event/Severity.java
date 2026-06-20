package com.eventagent.common.event;

/**
 * Severity levels for clinical events and agent decisions.
 *
 * <p>Each severity level carries a numeric {@code priority} value used for
 * ordering and prioritization. Lower numeric values indicate higher severity:
 * CRITICAL (1) is the most urgent, LOW (4) is the least.</p>
 *
 * <p>Severity drives routing decisions, notification urgency, and
 * processing order within the agent pipeline.</p>
 */
public enum Severity {

    /** Life-threatening condition requiring immediate intervention. Priority 1. */
    CRITICAL(1),

    /** Significant abnormality requiring prompt attention. Priority 2. */
    HIGH(2),

    /** Notable finding that warrants monitoring or follow-up. Priority 3. */
    MEDIUM(3),

    /** Informational or within normal limits. Priority 4. */
    LOW(4);

    private final int priority;

    Severity(int priority) {
        this.priority = priority;
    }

    /**
     * Returns the numeric priority for this severity level.
     *
     * <p>Lower values indicate higher severity. Useful for comparisons
     * and priority queue ordering.</p>
     *
     * @return the priority value (1 = most severe, 4 = least severe)
     */
    public int getPriority() {
        return priority;
    }
}
