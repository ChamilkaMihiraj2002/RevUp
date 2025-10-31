package com.revup.vehicle_service.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleResourceNotFoundException(
            ResourceNotFoundException ex, ServerWebExchange exchange) {
        Map<String, Object> error = new HashMap<>();
        error.put("status", HttpStatus.NOT_FOUND.value());
        error.put("error", "Not Found");
        error.put("message", ex.getMessage());
        error.put("timestamp", LocalDateTime.now());
        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(error));
    }
    
    @ExceptionHandler(DuplicateResourceException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleDuplicateResourceException(
            DuplicateResourceException ex, ServerWebExchange exchange) {
        Map<String, Object> error = new HashMap<>();
        error.put("status", HttpStatus.CONFLICT.value());
        error.put("error", "Conflict");
        error.put("message", ex.getMessage());
        error.put("timestamp", LocalDateTime.now());
        return Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).body(error));
    }
    
    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleValidationException(
            WebExchangeBindException ex, ServerWebExchange exchange) {
        Map<String, Object> error = new HashMap<>();
        error.put("status", HttpStatus.BAD_REQUEST.value());
        error.put("error", "Validation Failed");
        error.put("message", "Invalid input");
        error.put("timestamp", LocalDateTime.now());
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error));
    }
}
