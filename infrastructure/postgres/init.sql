-- ---------------------------------------------------------------------------
-- Event-Driven AI Agent Orchestration Platform
-- PostgreSQL Initialization Script
-- ---------------------------------------------------------------------------
-- This script runs automatically on first container startup via the
-- docker-entrypoint-initdb.d mechanism. It creates the four bounded-context
-- schemas used by the platform microservices.
-- ---------------------------------------------------------------------------

-- Create schemas for each bounded context
CREATE SCHEMA IF NOT EXISTS ingestion;
CREATE SCHEMA IF NOT EXISTS orchestrator;
CREATE SCHEMA IF NOT EXISTS action;
CREATE SCHEMA IF NOT EXISTS query;

-- Grant all privileges to the application user on each schema
GRANT ALL PRIVILEGES ON SCHEMA ingestion TO eventagent;
GRANT ALL PRIVILEGES ON SCHEMA orchestrator TO eventagent;
GRANT ALL PRIVILEGES ON SCHEMA action TO eventagent;
GRANT ALL PRIVILEGES ON SCHEMA query TO eventagent;

-- Set default privileges so future tables/sequences created in these schemas
-- are also accessible by the application user
ALTER DEFAULT PRIVILEGES IN SCHEMA ingestion
    GRANT ALL PRIVILEGES ON TABLES TO eventagent;
ALTER DEFAULT PRIVILEGES IN SCHEMA ingestion
    GRANT ALL PRIVILEGES ON SEQUENCES TO eventagent;

ALTER DEFAULT PRIVILEGES IN SCHEMA orchestrator
    GRANT ALL PRIVILEGES ON TABLES TO eventagent;
ALTER DEFAULT PRIVILEGES IN SCHEMA orchestrator
    GRANT ALL PRIVILEGES ON SEQUENCES TO eventagent;

ALTER DEFAULT PRIVILEGES IN SCHEMA action
    GRANT ALL PRIVILEGES ON TABLES TO eventagent;
ALTER DEFAULT PRIVILEGES IN SCHEMA action
    GRANT ALL PRIVILEGES ON SEQUENCES TO eventagent;

ALTER DEFAULT PRIVILEGES IN SCHEMA query
    GRANT ALL PRIVILEGES ON TABLES TO eventagent;
ALTER DEFAULT PRIVILEGES IN SCHEMA query
    GRANT ALL PRIVILEGES ON SEQUENCES TO eventagent;

-- Log confirmation
DO $$
BEGIN
    RAISE NOTICE 'All schemas created and privileges granted successfully.';
END
$$;
