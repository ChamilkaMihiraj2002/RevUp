package com.revup.time_tracking_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.ServerWebInputException;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> resourceNotFoundException(ResourceNotFoundException ex, ServerWebExchange exchange) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", Instant.now());
        body.put("message", ex.getMessage());
        // Use the request URI/path from the reactive ServerWebExchange
        body.put("details", exchange.getRequest().getURI().toString());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    /**
     * Handle malformed request bodies (e.g. invalid JSON) and similar decoding errors.
     * Spring WebFlux typically throws ServerWebInputException for these cases.
     */
    @ExceptionHandler(ServerWebInputException.class)
    public ResponseEntity<?> handleServerWebInputException(ServerWebInputException ex, ServerWebExchange exchange) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", Instant.now());
        // provide a clear client-facing message
        String message = ex.getReason() != null ? ex.getReason() : "Failed to read HTTP message";
        body.put("message", message);
        body.put("details", exchange.getRequest().getURI().toString() + " - " + ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle validation errors from @Valid annotated request bodies.
     * WebFlux binds and throws WebExchangeBindException when validation fails.
     */
    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<?> handleValidationException(WebExchangeBindException ex, ServerWebExchange exchange) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", Instant.now());
        body.put("message", "Validation failed");
        // collect field errors for client
        List<Map<String, Object>> errors = ex.getFieldErrors().stream()
                .map(err -> {
                    Map<String, Object> e = new HashMap<>();
                    e.put("field", err.getField());
                    e.put("rejectedValue", err.getRejectedValue());
                    e.put("message", err.getDefaultMessage());
                    return e;
                })
                .collect(Collectors.toList());
        body.put("errors", errors);
        body.put("details", exchange.getRequest().getURI().toString());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobalException(Exception ex, ServerWebExchange exchange) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", Instant.now());
        body.put("message", "Internal Server Error");
        // include request URI and exception message for context
        body.put("details", exchange.getRequest().getURI().toString() + " - " + ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}