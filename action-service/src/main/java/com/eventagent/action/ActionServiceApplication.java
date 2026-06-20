package com.eventagent.action;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Action Service - performs side effects from agent decisions.
 *
 * <p>Consumes triaged or decision events and performs the side effect
 * (record a notification, escalate). Idempotent on decision ID.
 * Has its own DLQ. The actual notification is a logged or persisted
 * record - no real paging provider integration.</p>
 */
@SpringBootApplication(scanBasePackages = {"com.eventagent.action", "com.eventagent.common"})
public class ActionServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ActionServiceApplication.class, args);
    }
}
