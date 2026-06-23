package com.eventagent.orchestrator.llm;

import com.eventagent.common.event.ClinicalEvent;
import com.eventagent.common.event.RoutingDecision;
import com.eventagent.common.event.Severity;

public interface LlmClient {

    record LlmResponse<T>(T result, int inputTokens, int outputTokens, long costMicros, long latencyMs) {}

    /**
     * Step 1: CLASSIFYING. Determine the severity of the clinical event.
     */
    LlmResponse<Severity> classifySeverity(ClinicalEvent event);

    /**
     * Step 2: SUMMARIZING. Generate a plain-language summary of the clinical finding.
     */
    LlmResponse<String> summarize(ClinicalEvent event, Severity severity);

    /**
     * Step 3: ROUTING. Decide the routing action.
     */
    LlmResponse<RoutingDecision> determineRouting(ClinicalEvent event, Severity severity, String summary);
}
