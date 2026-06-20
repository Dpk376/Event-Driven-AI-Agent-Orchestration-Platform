package com.eventagent.common.exception;

/**
 * Exception representing a failure when calling a Large Language Model (LLM) API.
 *
 * <p>Captures the HTTP status code from the LLM provider's response and whether
 * the failure is retryable. This distinction drives the error handling strategy:</p>
 * <ul>
 *   <li><b>Retryable</b> (e.g., 429 Too Many Requests, 503 Service Unavailable) -
 *       the caller should retry with exponential backoff.</li>
 *   <li><b>Non-retryable</b> (e.g., 400 Bad Request, 401 Unauthorized) -
 *       the caller should fail immediately and route to DLQ.</li>
 * </ul>
 *
 * <p>Static factory methods provide convenient construction for common failure modes
 * (rate limiting, server errors) without requiring callers to remember status codes.</p>
 *
 * @see RetryableException
 * @see NonRetryableException
 */
public class LlmException extends RuntimeException {

    private static final int HTTP_STATUS_TOO_MANY_REQUESTS = 429;
    private static final int HTTP_STATUS_INTERNAL_SERVER_ERROR = 500;

    private final int httpStatusCode;
    private final boolean retryable;

    /**
     * Constructs a new LLM exception.
     *
     * @param message        the detail message describing the LLM failure
     * @param httpStatusCode the HTTP status code from the LLM provider
     * @param retryable      whether this failure should be retried
     */
    public LlmException(String message, int httpStatusCode, boolean retryable) {
        super(message);
        this.httpStatusCode = httpStatusCode;
        this.retryable = retryable;
    }

    /**
     * Constructs a new LLM exception with an underlying cause.
     *
     * @param message        the detail message describing the LLM failure
     * @param httpStatusCode the HTTP status code from the LLM provider
     * @param retryable      whether this failure should be retried
     * @param cause          the underlying cause of the failure
     */
    public LlmException(String message, int httpStatusCode, boolean retryable, Throwable cause) {
        super(message, cause);
        this.httpStatusCode = httpStatusCode;
        this.retryable = retryable;
    }

    /**
     * Returns the HTTP status code from the LLM provider's response.
     *
     * @return the HTTP status code
     */
    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    /**
     * Returns whether this failure is transient and should be retried.
     *
     * @return {@code true} if the caller should retry, {@code false} if the failure is permanent
     */
    public boolean isRetryable() {
        return retryable;
    }

    // --- Static Factory Methods ---

    /**
     * Creates a retryable LLM exception for transient failures.
     *
     * @param message        the detail message
     * @param httpStatusCode the HTTP status code
     * @return a retryable LlmException
     */
    public static LlmException retryable(String message, int httpStatusCode) {
        return new LlmException(message, httpStatusCode, true);
    }

    /**
     * Creates a retryable LLM exception for transient failures with an underlying cause.
     *
     * @param message        the detail message
     * @param httpStatusCode the HTTP status code
     * @param cause          the underlying cause
     * @return a retryable LlmException
     */
    public static LlmException retryable(String message, int httpStatusCode, Throwable cause) {
        return new LlmException(message, httpStatusCode, true, cause);
    }

    /**
     * Creates a non-retryable LLM exception for permanent failures.
     *
     * @param message        the detail message
     * @param httpStatusCode the HTTP status code
     * @return a non-retryable LlmException
     */
    public static LlmException nonRetryable(String message, int httpStatusCode) {
        return new LlmException(message, httpStatusCode, false);
    }

    /**
     * Creates a non-retryable LLM exception for permanent failures with an underlying cause.
     *
     * @param message        the detail message
     * @param httpStatusCode the HTTP status code
     * @param cause          the underlying cause
     * @return a non-retryable LlmException
     */
    public static LlmException nonRetryable(String message, int httpStatusCode, Throwable cause) {
        return new LlmException(message, httpStatusCode, false, cause);
    }

    /**
     * Creates a retryable LLM exception for rate limiting (HTTP 429).
     *
     * <p>Rate-limited requests should always be retried with exponential backoff,
     * respecting any Retry-After header from the provider.</p>
     *
     * @param message the detail message describing the rate limit condition
     * @return a retryable LlmException with status code 429
     */
    public static LlmException rateLimited(String message) {
        return new LlmException(message, HTTP_STATUS_TOO_MANY_REQUESTS, true);
    }

    /**
     * Creates a retryable LLM exception for server-side errors (HTTP 500).
     *
     * <p>Server errors from the LLM provider are typically transient and should
     * be retried with backoff.</p>
     *
     * @param message the detail message describing the server error
     * @param cause   the underlying cause (e.g., IOException from the HTTP client)
     * @return a retryable LlmException with status code 500
     */
    public static LlmException serverError(String message, Throwable cause) {
        return new LlmException(message, HTTP_STATUS_INTERNAL_SERVER_ERROR, true, cause);
    }
}
