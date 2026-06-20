# Low-Level Design (LLD)

## 1. Database Schemas

### 1.1 Ingestion Service Schema (`ingestion`)
* `clinical_event`: Stores the raw payload. Unique constraint on `idempotency_key`.
* `outbox_event`: Standard outbox table. `status` column enum (`PENDING`, `PUBLISHED`).

### 1.2 Orchestrator Service Schema (`orchestrator`)
* `processed_event`: Idempotency tracking table. Primary key is `event_id`.
* `event_state`: State machine tracker (`CLASSIFYING`, `SUMMARIZING`, `ROUTING`, `COMPLETED`, `FAILED`).
* `agent_decision`: The persisted AI analysis and routing output.

### 1.3 Query Service Schema (`query`)
* `decision_view`: Highly denormalized table heavily indexed for UI dashboards. Includes JSONB columns for fast filtering.

## 2. LLM Resiliency Flow

When `orchestrator-service` calls the LLM:
1. **Token Budget Check:** It attempts to execute `INCRBY` on a Redis key representing the current minute's budget. If the increment exceeds `TOKEN_BUDGET_PER_MINUTE`, the Kafka Consumer is paused, and the message is nacked/re-queued.
2. **Circuit Breaker Check:** If the breaker is `OPEN`, a `CallNotPermittedException` is thrown, triggering the exact same consumer pause logic.
3. **Execution:** The HTTP call is made to OpenAI.
4. **Retry/Jitter:** If a 429 or 5xx occurs, Resilience4j executes an exponential backoff retry. We use **Full Jitter** (`random * (base * 2^attempt)`) to avoid thundering herd problems when the LLM service recovers.
5. **Reconciliation:** Once the response arrives, the exact `total_tokens` consumed is reported back to the Redis Token Budget to true-up the estimate.

## 3. Kafka Topology & Semantics

### 3.1 Topics
* `clinical.events.raw` (Partitions: 6) - Producer: Ingestion. Consumer: Orchestrator.
* `clinical.events.triaged` (Partitions: 6) - Producer: Orchestrator. Consumer: Action.
* `agent.decisions` (Partitions: 6) - Producer: Orchestrator. Consumer: Query.

### 3.2 Dead Letter Queues (DLQ)
Every consumer uses Spring Kafka's `DefaultErrorHandler` paired with a `DeadLetterPublishingRecoverer`.
* **Retryable Exceptions** (e.g., transient DB locks) trigger local retries.
* **Non-Retryable Exceptions** (e.g., invalid JSON, permanent null pointers) instantly route the payload to `{topic_name}.DLT` and commit the offset on the main topic.

### 3.3 Consumer Semantics
We disable auto-commit (`enable.auto.commit=false`) and use `AckMode.MANUAL`. The offset is **only** acknowledged manually by the application code after the database transaction corresponding to the agent's decision has successfully committed. This provides **At-Least-Once** delivery, which we upgrade to **Exactly-Once** via our idempotent database schemas.
