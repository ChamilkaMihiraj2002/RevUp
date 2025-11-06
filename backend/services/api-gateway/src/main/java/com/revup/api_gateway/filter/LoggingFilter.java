package com.revup.api_gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.ServerRequest;

/**
 * Custom Gateway Filter for logging and adding custom headers
 */
@Component
public class LoggingFilter {

    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

    /**
     * Log incoming requests
     */
    public static void logRequest(ServerRequest request) {
        logger.info("Incoming request: {} {}", request.method(), request.uri());
        logger.debug("Request Headers: {}", request.headers().asHttpHeaders());
    }

    /**
     * Add custom headers to outgoing requests
     */
    public static ServerRequest addCustomHeaders(ServerRequest request) {
        logRequest(request);
        return request;
    }
}
