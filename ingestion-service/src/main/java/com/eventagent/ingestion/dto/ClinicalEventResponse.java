package com.eventagent.ingestion.dto;

public record ClinicalEventResponse(
        String eventId,
        String status,
        String message
) {}
