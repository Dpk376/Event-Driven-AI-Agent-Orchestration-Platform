# Project Milestones

The development of the platform is broken down into 7 distinct milestones to ensure incremental delivery of value with high architectural rigor.

### ✅ Milestone 0: Scaffolding
- [x] Multi-module Gradle build system (`common`, `ingestion-service`, `orchestrator-service`, etc.).
- [x] `docker-compose.yml` defining KRaft Kafka, Postgres, Redis, OpenTelemetry, Jaeger, Prometheus, Grafana.
- [x] PostgreSQL schemas initialization via Flyway.
- [x] `common` module establishing shared Event Contracts (`ClinicalEvent`), Exception hierarchies, and Tracing constants.

### ✅ Milestone 1: Ingestion Service
- [x] REST API Controller for clinical events (`POST /api/v1/events`).
- [x] Strict API Key authentication using `OncePerRequestFilter`.
- [x] Fast-path and fallback Idempotency handling (Duplicate requests yield `202 Accepted` but zero duplicate processing).
- [x] **Transactional Outbox Pattern**: Atomic commit of incoming event + outbox entry, with a scheduled Relay pushing to Kafka `clinical.events.raw`.

### ⏳ Milestone 2: Orchestrator Skeleton
- [ ] Kafka listener for `clinical.events.raw` with **manual offset commit** and Dead Letter Queue (DLQ) routing.
- [ ] Redis-backed deduplication (SETNX) before processing.
- [ ] AI Agent State Machine (`CLASSIFYING` -> `SUMMARIZING` -> `ROUTING` -> `ACTING`).
- [ ] `FakeLlmClient` implementation for deterministic and free integration testing.
- [ ] Persist final decisions to Postgres and publish to `agent.decisions` topic.

### ⏳ Milestone 3: LLM Resilience (Core Feature)
- [ ] Implement `OpenAiLlmClient`.
- [ ] Wrap LLM calls with Resilience4j **Circuit Breaker** (50% failure threshold) and **Retry** (Exponential backoff with full jitter).
- [ ] **Token Budgeting**: Redis-based token limit tracker to prevent quota exhaustion.
- [ ] **Kafka Backpressure**: Dynamically pause/resume Kafka consumers when the circuit breaker opens or token budget is depleted.

### ⏳ Milestone 4: Action Service
- [ ] Consume from `clinical.events.triaged`.
- [ ] Execute idempotent notifications (Logging/Mock integrations).
- [ ] Handle persistent failures via a dedicated Action DLQ.

### ⏳ Milestone 5: Query Service & Observability
- [ ] Consume `agent.decisions` to build an eventually consistent CQRS read-model.
- [ ] Provide REST endpoints for dashboard queries.
- [ ] Finalize Grafana dashboards (End-to-end latency, LLM cost tracking, DLQ volume).
- [ ] Provide a `k6` load-test script to prove the ingestion layer handles 1,000 req/sec.

### ⏳ Milestone 6: Hardening and Documentation
- [ ] Chaos testing (kill Kafka, kill Redis) to verify Outbox recovery and degraded mode handling.
- [ ] Produce End-to-End trace verification across Jaeger.
- [ ] Final architectural defense writeup.
