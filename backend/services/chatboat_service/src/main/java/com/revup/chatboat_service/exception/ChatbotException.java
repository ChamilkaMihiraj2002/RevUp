package com.revup.chatboat_service.exception;

/**
 * Generic chatbot runtime exception for unexpected errors inside chatbot processing.
 */
public class ChatbotException extends RuntimeException {
    public ChatbotException(String message) {
        super(message);
    }

    public ChatbotException(String message, Throwable cause) {
        super(message, cause);
    }
}
