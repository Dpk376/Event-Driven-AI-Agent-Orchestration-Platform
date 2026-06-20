package com.eventagent.ingestion.entity;

import com.eventagent.common.event.ClinicalEventType;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "clinical_event", schema = "ingestion")
public class ClinicalEventEntity {

    @Id
    private UUID id;

    @Column(name = "event_id", nullable = false)
    private String eventId;

    @Column(name = "patient_id", nullable = false)
    private String patientId;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false)
    private ClinicalEventType eventType;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "payload", columnDefinition = "jsonb", nullable = false)
    private Map<String, Object> payload;

    @Column(name = "idempotency_key", nullable = false, unique = true)
    private String idempotencyKey;

    @Column(name = "timestamp", nullable = false)
    private Instant timestamp;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    protected ClinicalEventEntity() {
        // JPA requires a no-arg constructor
    }

    public ClinicalEventEntity(UUID id, String eventId, String patientId, ClinicalEventType eventType, Map<String, Object> payload, String idempotencyKey, Instant timestamp) {
        this.id = id;
        this.eventId = eventId;
        this.patientId = patientId;
        this.eventType = eventType;
        this.payload = payload;
        this.idempotencyKey = idempotencyKey;
        this.timestamp = timestamp;
    }

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = Instant.now();
        }
    }

    // Getters

    public UUID getId() {
        return id;
    }

    public String getEventId() {
        return eventId;
    }

    public String getPatientId() {
        return patientId;
    }

    public ClinicalEventType getEventType() {
        return eventType;
    }

    public Map<String, Object> getPayload() {
        return payload;
    }

    public String getIdempotencyKey() {
        return idempotencyKey;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
