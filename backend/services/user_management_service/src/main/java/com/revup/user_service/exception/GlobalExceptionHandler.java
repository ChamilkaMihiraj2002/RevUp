package com.revup.user_service.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
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
    public Mono<ResponseEntity<ErrorResponse>> handleResourceNotFoundException(
            ResourceNotFoundException ex, ServerWebExchange exchange) {
        
        log.error("Resource not found: {}", ex.getMessage());
        
        ErrorResponse error = ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                .message(ex.getMessage())
                .path(exchange.getRequest().getPath().value())
                .timestamp(LocalDateTime.now())
                .build();
        
        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(error));
    }
    
    @ExceptionHandler(DuplicateResourceException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleDuplicateResourceException(
            DuplicateResourceException ex, ServerWebExchange exchange) {
        
        log.error("Duplicate resource: {}", ex.getMessage());
        
        ErrorResponse error = ErrorResponse.builder()
                .status(HttpStatus.CONFLICT.value())
                .error(HttpStatus.CONFLICT.getReasonPhrase())
                .message(ex.getMessage())
                .path(exchange.getRequest().getPath().value())
                .timestamp(LocalDateTime.now())
                .build();
        
        return Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).body(error));
    }
    
    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleValidationException(
            WebExchangeBindException ex, ServerWebExchange exchange) {
        
        log.error("Validation error: {}", ex.getMessage());
        
        Map<String, String> validationErrors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            validationErrors.put(fieldName, errorMessage);
        });
        
        ErrorResponse error = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message("Validation failed")
                .path(exchange.getRequest().getPath().value())
                .timestamp(LocalDateTime.now())
                .validationErrors(validationErrors)
                .build();
        
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error));
    }
    
    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ErrorResponse>> handleGenericException(
            Exception ex, ServerWebExchange exchange) {
        
        log.error("Unexpected error: ", ex);
        
        ErrorResponse error = ErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .message("An unexpected error occurred")
                .path(exchange.getRequest().getPath().value())
                .timestamp(LocalDateTime.now())
                .build();
        
        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error));
    }
}
