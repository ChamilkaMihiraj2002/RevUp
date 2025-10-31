package com.revup.service_management_service.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.revup.service_management_service.dto.ServiceInstanceResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class ServiceProgressWebSocketHandler implements WebSocketHandler {

    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final Sinks.Many<String> progressSink = Sinks.many().multicast().onBackpressureBuffer();
    private final ObjectMapper objectMapper;

    public ServiceProgressWebSocketHandler() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        String sessionId = session.getId();
        log.info("WebSocket session connected: {}", sessionId);
        sessions.put(sessionId, session);

        return session.send(
                progressSink.asFlux()
                        .map(session::textMessage)
        ).doFinally(signalType -> {
            sessions.remove(sessionId);
            log.info("WebSocket session disconnected: {}, signal: {}", sessionId, signalType);
        });
    }

    public void broadcastProgress(ServiceInstanceResponse progress) {
        try {
            String message = objectMapper.writeValueAsString(progress);
            log.debug("Broadcasting progress update: {}", message);
            progressSink.tryEmitNext(message);
        } catch (JsonProcessingException e) {
            log.error("Error serializing progress update", e);
        }
    }

    public int getActiveSessionsCount() {
        return sessions.size();
    }
}
