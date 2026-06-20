package com.eventagent.common.exception;

/**
 * Indicates a transient failure that should be retried.
 *
 * <p>Kafka consumers configured with a {@code DefaultErrorHandler} should
 * retry messages that fail with this exception type using exponential backoff.
 * Typical causes include temporary network issues, database connection timeouts,
 * or upstream service unavailability.</p>
 *
 * <p>After all retry attempts are exhausted, the message should be forwarded
 * to the appropriate Dead Letter Topic (DLT) for manual investigation.</p>
 *
 * @see NonRetryableException for permanent failures that skip retries
 */
public class RetryableException extends RuntimeException {

    /**
     * Constructs a new retryable exception with the specified detail message.
     *
     * @param message the detail message describing the transient failure
     */
    public RetryableException(String message) {
        super(message);
    }

    /**
     * Constructs a new retryable exception with the specified detail message and cause.
     *
     * @param message the detail message describing the transient failure
     * @param cause   the underlying cause of the failure
     */
    public RetryableException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new retryable exception with the specified cause.
     *
     * @param cause the underlying cause of the failure
     */
    public RetryableException(Throwable cause) {
        super(cause);
    }
}
