package com.eventagent.ingestion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Ingestion Service - REST ingress for synthetic clinical events.
 *
 * <p>Validates input at the boundary, assigns idempotency keys, writes
 * event and outbox rows in a single transaction, and relays to Kafka
 * via the outbox pattern. This guarantees no event is lost even if
 * the broker is temporarily unreachable after commit.</p>
 */
@SpringBootApplication(scanBasePackages = {"com.eventagent.ingestion", "com.eventagent.common"})
@EnableScheduling
public class IngestionServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(IngestionServiceApplication.class, args);
    }
}
