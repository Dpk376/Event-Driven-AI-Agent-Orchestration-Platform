CREATE TABLE clinical_event (
    id UUID PRIMARY KEY,
    event_id VARCHAR(255) NOT NULL,
    patient_id VARCHAR(255) NOT NULL,
    event_type VARCHAR(100) NOT NULL,
    payload JSONB NOT NULL,
    idempotency_key VARCHAR(255) NOT NULL UNIQUE,
    timestamp TIMESTAMP WITH TIME ZONE NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_clinical_event_patient_id ON clinical_event(patient_id);
