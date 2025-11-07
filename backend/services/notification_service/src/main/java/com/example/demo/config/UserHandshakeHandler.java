package com.example.demo.config;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

/**
 * Simple HandshakeHandler that attaches a Principal based on a query parameter `username`.
 *
 * Usage: connect to /websocket?username=alice so the WebSocket session will have Principal.getName()=="alice".
 * This is intended for development/testing when you don't have full authentication.
 */
public class UserHandshakeHandler extends DefaultHandshakeHandler {

    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        String query = request.getURI().getQuery();
        if (query != null) {
            for (String part : query.split("&")) {
                String[] kv = part.split("=", 2);
                if (kv.length == 2 && "username".equalsIgnoreCase(kv[0]) && kv[1] != null && !kv[1].isEmpty()) {
                    return new StompPrincipal(kv[1]);
                }
            }
        }
        // fallback to default behavior (no principal)
        return super.determineUser(request, wsHandler, attributes);
    }
}