package com.eventagent.query;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Query Service - CQRS read model for agent decisions and traces.
 *
 * <p>Consumes decision events and builds queryable views. Exposes
 * read-only REST endpoints. Eventually consistent by design (AP).
 * This is the query side of the CQRS split - it never writes to
 * the command-side stores.</p>
 */
@SpringBootApplication(scanBasePackages = {"com.eventagent.query", "com.eventagent.common"})
public class QueryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(QueryServiceApplication.class, args);
    }
}
