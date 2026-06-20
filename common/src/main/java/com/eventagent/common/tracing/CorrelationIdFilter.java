package com.eventagent.common.tracing;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

/**
 * Servlet filter that ensures every HTTP request has a correlation ID for distributed tracing.
 *
 * <p>This filter runs at the highest precedence to guarantee that all downstream
 * filters, interceptors, and controllers have access to the correlation ID in the
 * SLF4J MDC (Mapped Diagnostic Context). This enables structured logging with
 * correlation IDs across all log statements for a given request.</p>
 *
 * <p>Behavior:</p>
 * <ol>
 *   <li>Reads the {@code X-Correlation-ID} header from the incoming request.</li>
 *   <li>If the header is absent or blank, generates a new UUID as the correlation ID.</li>
 *   <li>Sets the correlation ID in the SLF4J MDC under the key defined by
 *       {@link TracingConstants#MDC_CORRELATION_ID}.</li>
 *   <li>Adds the correlation ID as a response header ({@code X-Correlation-ID})
 *       so callers can correlate their request with server-side logs.</li>
 *   <li>Clears the MDC in a finally block to prevent context leakage between
 *       requests on pooled threads.</li>
 * </ol>
 *
 * <p>This filter is annotated as a Spring {@link Component} so it is automatically
 * registered by any service that component-scans the {@code com.eventagent.common}
 * package.</p>
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorrelationIdFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(CorrelationIdFilter.class);

    /**
     * Processes the request by establishing a correlation ID in the MDC.
     *
     * @param request     the incoming HTTP request
     * @param response    the outgoing HTTP response
     * @param filterChain the filter chain to continue processing
     * @throws ServletException if the filter chain throws a servlet exception
     * @throws IOException      if the filter chain throws an I/O exception
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String correlationId = resolveCorrelationId(request);
            MDC.put(TracingConstants.MDC_CORRELATION_ID, correlationId);
            response.setHeader(TracingConstants.HEADER_CORRELATION_ID, correlationId);

            log.debug("Correlation ID established for request: method={}, uri={}",
                    request.getMethod(), request.getRequestURI());

            filterChain.doFilter(request, response);
        } finally {
            MDC.remove(TracingConstants.MDC_CORRELATION_ID);
        }
    }

    /**
     * Extracts the correlation ID from the request header, or generates a new one if absent.
     *
     * @param request the incoming HTTP request
     * @return the correlation ID, never null or blank
     */
    private String resolveCorrelationId(HttpServletRequest request) {
        String headerValue = request.getHeader(TracingConstants.HEADER_CORRELATION_ID);
        if (headerValue != null && !headerValue.isBlank()) {
            return headerValue.trim();
        }
        String generatedCorrelationId = UUID.randomUUID().toString();
        log.debug("No {} header found, generated new correlation ID: {}",
                TracingConstants.HEADER_CORRELATION_ID, generatedCorrelationId);
        return generatedCorrelationId;
    }
}
