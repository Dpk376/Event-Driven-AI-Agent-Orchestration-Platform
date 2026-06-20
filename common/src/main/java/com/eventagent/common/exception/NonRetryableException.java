package com.eventagent.common.exception;

/**
 * Indicates a permanent failure that should not be retried.
 *
 * <p>Kafka consumers configured with a {@code DefaultErrorHandler} should
 * immediately forward messages that fail with this exception to the
 * Dead Letter Topic (DLT) without any retry attempts. Typical causes
 * include malformed payloads, schema validation failures, or business
 * rule violations that no amount of retrying will resolve.</p>
 *
 * @see RetryableException for transient failures that should be retried
 */
public class NonRetryableException extends RuntimeException {

    /**
     * Constructs a new non-retryable exception with the specified detail message.
     *
     * @param message the detail message describing the permanent failure
     */
    public NonRetryableException(String message) {
        super(message);
    }

    /**
     * Constructs a new non-retryable exception with the specified detail message and cause.
     *
     * @param message the detail message describing the permanent failure
     * @param cause   the underlying cause of the failure
     */
    public NonRetryableException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new non-retryable exception with the specified cause.
     *
     * @param cause the underlying cause of the failure
     */
    public NonRetryableException(Throwable cause) {
        super(cause);
    }
}
