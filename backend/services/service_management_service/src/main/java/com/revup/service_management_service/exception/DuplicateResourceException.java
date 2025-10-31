package com.revup.service_management_service.exception;

import lombok.Getter;

@Getter
public class DuplicateResourceException extends RuntimeException {
    
    private final String message;

    public DuplicateResourceException(String message) {
        super(message);
        this.message = message;
    }
}
