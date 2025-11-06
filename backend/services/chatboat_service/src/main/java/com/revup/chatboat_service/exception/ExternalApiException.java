package com.revup.chatboat_service.exception;

/**
 * Exception thrown when the external microservice call fails or returns an unexpected result.
 */
public class ExternalApiException extends RuntimeException {
    public ExternalApiException(String message) {
        super(message);
    }

    public ExternalApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
