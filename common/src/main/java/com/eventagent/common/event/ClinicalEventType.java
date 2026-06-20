package com.eventagent.common.event;

/**
 * Defines the types of clinical events that flow through the platform.
 *
 * <p>Each event type represents a distinct clinical data category
 * originating from hospital information systems, EHRs, or medical devices.
 * The {@code displayName} provides a human-readable label suitable for
 * logging, UI rendering, and notification messages.</p>
 */
public enum ClinicalEventType {

    /** Laboratory test result - blood work, urinalysis, cultures, etc. */
    LAB_RESULT("Lab Result"),

    /** Vital sign reading - heart rate, blood pressure, SpO2, temperature, etc. */
    VITAL_SIGN("Vital Sign"),

    /** Patient admission to a unit or facility. */
    ADMISSION("Admission"),

    /** Patient discharge from a unit or facility. */
    DISCHARGE("Discharge");

    private final String displayName;

    ClinicalEventType(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Returns a human-readable display name for this event type.
     *
     * @return the display name, never null
     */
    public String getDisplayName() {
        return displayName;
    }
}
