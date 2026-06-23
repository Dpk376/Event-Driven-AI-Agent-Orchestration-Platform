package com.eventagent.orchestrator.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class EventDeduplicator {

    private static final Logger log = LoggerFactory.getLogger(EventDeduplicator.class);
    private static final Duration TTL = Duration.ofDays(7);
    private static final String KEY_PREFIX = "orchestrator:event:";

    private final StringRedisTemplate redisTemplate;

    public EventDeduplicator(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * Checks if an event with the given idempotency key has already been processed.
     * Uses Redis SETNX (setIfAbsent) to ensure atomic deduplication.
     *
     * @param idempotencyKey the unique key for the event
     * @return true if this is a new event and should be processed, false if it's a duplicate
     */
    public boolean isNewEvent(String idempotencyKey) {
        String key = KEY_PREFIX + idempotencyKey;
        Boolean isNew = redisTemplate.opsForValue().setIfAbsent(key, "processed", TTL);
        
        if (Boolean.TRUE.equals(isNew)) {
            log.debug("Event with idempotencyKey {} is new.", idempotencyKey);
            return true;
        } else {
            log.warn("Duplicate event detected. IdempotencyKey {} already processed.", idempotencyKey);
            return false;
        }
    }
}
