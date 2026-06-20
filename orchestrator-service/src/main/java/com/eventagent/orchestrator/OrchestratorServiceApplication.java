package com.eventagent.orchestrator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Orchestrator Service - the heart of the agent pipeline.
 *
 * <p>Consumes raw clinical events from Kafka, deduplicates, drives a
 * deterministic state machine through the agent pipeline (classify,
 * summarize, route, act), calls the LLM through a guarded client,
 * and emits decision and triaged events. Poison messages route to
 * a DLQ. Every agent step is a trace span carrying token and cost
 * attributes.</p>
 */
@SpringBootApplication(scanBasePackages = {"com.eventagent.orchestrator", "com.eventagent.common"})
public class OrchestratorServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrchestratorServiceApplication.class, args);
    }
}
