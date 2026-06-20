package com.eventagent.common.event;

/**
 * Routing decisions made by AI agents after evaluating a clinical event.
 *
 * <p>Each decision type determines how the downstream pipeline handles
 * the event - whether it flows to standard routing, gets escalated to
 * a human reviewer, triggers a notification, or is dropped.</p>
 */
public enum RoutingDecision {

    /** Route the event through the standard clinical workflow. */
    ROUTE("Route through standard clinical workflow"),

    /** Escalate to a human reviewer or senior clinician for urgent attention. */
    ESCALATE("Escalate to human reviewer for urgent attention"),

    /** Send a notification to the appropriate care team or recipient. */
    NOTIFY("Send notification to care team or recipient"),

    /** No further action required - event is informational only. */
    NO_ACTION("No further action required");

    private final String description;

    RoutingDecision(String description) {
        this.description = description;
    }

    /**
     * Returns a human-readable description of this routing decision.
     *
     * @return the description, never null
     */
    public String getDescription() {
        return description;
    }
}
