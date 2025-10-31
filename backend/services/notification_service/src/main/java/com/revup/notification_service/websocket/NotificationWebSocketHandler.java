package com.revup.notification_service.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.revup.notification_service.dto.NotificationResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class NotificationWebSocketHandler implements WebSocketHandler {
    
    private final Sinks.Many<NotificationResponse> notificationSink;
    private final ObjectMapper objectMapper;
    private final Map<String, WebSocketSession> activeSessions = new ConcurrentHashMap<>();
    
    public NotificationWebSocketHandler() {
        this.notificationSink = Sinks.many().multicast().onBackpressureBuffer();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }
    
    @Override
    public Mono<Void> handle(WebSocketSession session) {
        String sessionId = session.getId();
        log.info("WebSocket connection established: {}", sessionId);
        
        activeSessions.put(sessionId, session);
        
        Flux<WebSocketMessage> outputMessages = notificationSink.asFlux()
                .map(notification -> {
                    try {
                        String json = objectMapper.writeValueAsString(notification);
                        return session.textMessage(json);
                    } catch (Exception e) {
                        log.error("Error serializing notification", e);
                        return session.textMessage("{\"error\":\"Serialization error\"}");
                    }
                })
                .doOnError(error -> log.error("Error in notification stream", error))
                .doFinally(signalType -> {
                    activeSessions.remove(sessionId);
                    log.info("WebSocket connection closed: {} - {}", sessionId, signalType);
                });
        
        return session.send(outputMessages);
    }
    
    public void broadcastNotification(NotificationResponse notification) {
        log.info("Broadcasting notification to {} active sessions", activeSessions.size());
        notificationSink.tryEmitNext(notification);
    }
    
    public int getActiveSessionCount() {
        return activeSessions.size();
    }
}
