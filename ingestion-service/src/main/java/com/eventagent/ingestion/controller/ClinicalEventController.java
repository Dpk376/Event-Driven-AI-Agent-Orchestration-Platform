package com.eventagent.ingestion.controller;

import com.eventagent.common.metrics.MetricNames;
import com.eventagent.ingestion.dto.ClinicalEventRequest;
import com.eventagent.ingestion.dto.ClinicalEventResponse;
import com.eventagent.ingestion.service.IngestionService;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/events")
public class ClinicalEventController {

    private final IngestionService ingestionService;
    private final MeterRegistry meterRegistry;

    public ClinicalEventController(IngestionService ingestionService, MeterRegistry meterRegistry) {
        this.ingestionService = ingestionService;
        this.meterRegistry = meterRegistry;
    }

    @PostMapping
    public ResponseEntity<ClinicalEventResponse> ingestEvent(@Valid @RequestBody ClinicalEventRequest request) {
        String eventId = ingestionService.ingestEvent(request);

        meterRegistry.counter(MetricNames.EVENTS_INGESTED_TOTAL,
                MetricNames.TAG_EVENT_TYPE, request.eventType().name()
        ).increment();

        ClinicalEventResponse response = new ClinicalEventResponse(eventId, "ACCEPTED", "Event successfully ingested");
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }
}
