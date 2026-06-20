package com.eventagent.ingestion.dto;

import com.eventagent.common.event.ClinicalEventType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Map;

public record ClinicalEventRequest(
        @NotBlank(message = "patientId is required")
        String patientId,

        @NotNull(message = "eventType is required")
        ClinicalEventType eventType,

        @NotNull(message = "payload is required")
        Map<String, Object> payload,

        @NotBlank(message = "idempotencyKey is required")
        String idempotencyKey
) {}
