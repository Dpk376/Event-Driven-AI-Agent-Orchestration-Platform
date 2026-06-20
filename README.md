# Event-Driven AI Agent Orchestration Platform

A production-grade, event-driven platform where clinical events flow through Kafka and a pipeline of LLM-powered agents consume them asynchronously to classify, summarize, decide, and act. The failure handling — not the LLM call — is the headline feature.

## ⚠️ Important Disclaimers
* **Synthetic Data Only:** This system processes synthetic data only. No real clinical decisions are made, no real patient data is used, and no real notifications are delivered.
* **Shared Database Pattern:** All microservices share one PostgreSQL instance with schema-per-service isolation for ease of local orchestration. In a production environment, each service would ideally own its dedicated database instance.

## 🏗️ Architecture Overview

The platform uses an event-driven microservices architecture communicating exclusively via Apache Kafka (KRaft mode). The system comprises four primary services:

1. **Ingestion Service:** A REST entry point that receives synthetic clinical events. It validates payloads, checks idempotency keys against PostgreSQL, and uses the **Transactional Outbox Pattern** to reliably publish events to the `clinical.events.raw` Kafka topic.
2. **Orchestrator Service:** The brain of the platform. It consumes raw events, drives an AI Agent state machine (Classify -> Summarize -> Route -> Act), and interacts with external LLMs (OpenAI/Anthropic). It is fortified with Resilience4j circuit breakers, retries, and a Redis-backed token budget to prevent LLM quota exhaustion.
3. **Action Service:** Consumes AI triaged decisions and routes them to outbound channels (e.g., paging, email). Designed for highly idempotent processing to prevent duplicate alerts.
4. **Query Service:** Constructs an eventually consistent (AP) read model of agent decisions via CQRS to power front-end dashboards.

> See `docs/HLD.md` and `docs/LLD.md` for in-depth design decisions and CAP theorem analysis.

## 🛠️ Technology Stack
* **Language:** Java 21 (Records, Sealed Classes)
* **Framework:** Spring Boot 3.4.x
* **Build System:** Gradle (Kotlin DSL)
* **Messaging:** Apache Kafka 3.8.x (KRaft Mode)
* **Database:** PostgreSQL 16 (Schema-per-service isolation)
* **Cache/Rate Limiting:** Redis 7
* **Resilience:** Resilience4j (Circuit Breakers, Retry, RateLimit)
* **Observability:** OpenTelemetry Collector, Jaeger (Tracing), Prometheus + Grafana (Metrics)
* **Testing:** JUnit 5, Mockito, Testcontainers

## 🚀 Quick Start (Local Development)

### Prerequisites
* Docker & Docker Compose
* Java 21
* Gradle

### Running the Infrastructure
1. Copy the environment variables:
   ```bash
   cp .env.example .env
   ```
2. Start the backing services (Kafka, Postgres, Redis, Observability):
   ```bash
   docker compose up -d
   ```
3. Build the microservices:
   ```bash
   ./gradlew build
   ```

### Accessing Observability UIs
Once the infrastructure is running:
* **Grafana:** `http://localhost:3000` (admin/admin)
* **Jaeger:** `http://localhost:16686`
* **Prometheus:** `http://localhost:9090`
