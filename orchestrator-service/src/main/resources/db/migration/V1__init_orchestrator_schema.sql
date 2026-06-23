CREATE SCHEMA IF NOT EXISTS orchestrator;

CREATE TABLE orchestrator.agent_decisions (
    id BIGSERIAL PRIMARY KEY,
    decision_id VARCHAR(36) NOT NULL UNIQUE,
    event_id VARCHAR(36) NOT NULL,
    patient_id VARCHAR(255) NOT NULL,
    severity VARCHAR(50) NOT NULL,
    summary TEXT,
    decision VARCHAR(50) NOT NULL,
    reason TEXT,
    agent_name VARCHAR(100) NOT NULL,
    pipeline_step VARCHAR(100) NOT NULL,
    tokens_input INTEGER NOT NULL,
    tokens_output INTEGER NOT NULL,
    cost_micros BIGINT NOT NULL,
    latency_ms BIGINT NOT NULL,
    trace_id VARCHAR(255),
    correlation_id VARCHAR(255),
    timestamp TIMESTAMP WITH TIME ZONE NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_agent_decisions_event_id ON orchestrator.agent_decisions(event_id);
CREATE INDEX idx_agent_decisions_patient_id ON orchestrator.agent_decisions(patient_id);
