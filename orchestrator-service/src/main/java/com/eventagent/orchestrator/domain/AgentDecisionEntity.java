package com.eventagent.orchestrator.domain;

import com.eventagent.common.event.RoutingDecision;
import com.eventagent.common.event.Severity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "agent_decisions", schema = "orchestrator")
public class AgentDecisionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "decision_id", nullable = false, unique = true, length = 36)
    private String decisionId;

    @Column(name = "event_id", nullable = false, length = 36)
    private String eventId;

    @Column(name = "patient_id", nullable = false)
    private String patientId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private Severity severity;

    @Column(columnDefinition = "TEXT")
    private String summary;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private RoutingDecision decision;

    @Column(columnDefinition = "TEXT")
    private String reason;

    @Column(name = "agent_name", nullable = false, length = 100)
    private String agentName;

    @Column(name = "pipeline_step", nullable = false, length = 100)
    private String pipelineStep;

    @Column(name = "tokens_input", nullable = false)
    private int tokensInput;

    @Column(name = "tokens_output", nullable = false)
    private int tokensOutput;

    @Column(name = "cost_micros", nullable = false)
    private long costMicros;

    @Column(name = "latency_ms", nullable = false)
    private long latencyMs;

    @Column(name = "trace_id")
    private String traceId;

    @Column(name = "correlation_id")
    private String correlationId;

    @Column(nullable = false)
    private Instant timestamp;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = Instant.now();
        }
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDecisionId() {
        return decisionId;
    }

    public void setDecisionId(String decisionId) {
        this.decisionId = decisionId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public Severity getSeverity() {
        return severity;
    }

    public void setSeverity(Severity severity) {
        this.severity = severity;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public RoutingDecision getDecision() {
        return decision;
    }

    public void setDecision(RoutingDecision decision) {
        this.decision = decision;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getPipelineStep() {
        return pipelineStep;
    }

    public void setPipelineStep(String pipelineStep) {
        this.pipelineStep = pipelineStep;
    }

    public int getTokensInput() {
        return tokensInput;
    }

    public void setTokensInput(int tokensInput) {
        this.tokensInput = tokensInput;
    }

    public int getTokensOutput() {
        return tokensOutput;
    }

    public void setTokensOutput(int tokensOutput) {
        this.tokensOutput = tokensOutput;
    }

    public long getCostMicros() {
        return costMicros;
    }

    public void setCostMicros(long costMicros) {
        this.costMicros = costMicros;
    }

    public long getLatencyMs() {
        return latencyMs;
    }

    public void setLatencyMs(long latencyMs) {
        this.latencyMs = latencyMs;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
