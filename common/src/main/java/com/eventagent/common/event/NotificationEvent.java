package com.eventagent.common.event;

import java.time.Instant;

/**
 * Represents a notification to be sent to a care team member, patient, or escalation channel.
 *
 * <p>Published to the {@code notifications.outbound} Kafka topic by the notification
 * service after a routing decision requires human attention. The {@code channel}
 * determines the delivery mechanism (e.g., care team dashboard, pager escalation,
 * patient portal notification).</p>
 *
 * @param notificationId unique identifier for this notification (UUID format)
 * @param decisionId     the ID of the agent decision that triggered this notification
 * @param patientId      the patient associated with this notification
 * @param channel        delivery channel identifier (e.g., "care_team", "escalation", "patient_notify")
 * @param recipient      the intended recipient (e.g., clinician ID, team name, patient ID)
 * @param message        the notification message content
 * @param correlationId  correlation ID linking this notification to the originating event flow
 * @param traceId        distributed trace ID for end-to-end observability
 * @param timestamp      when this notification was created
 */
public record NotificationEvent(
        String notificationId,
        String decisionId,
        String patientId,
        String channel,
        String recipient,
        String message,
        String correlationId,
        String traceId,
        Instant timestamp
) {
}
