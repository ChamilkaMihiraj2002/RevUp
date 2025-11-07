package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // enable both topic (broadcast) and queue (user-specific) destinations
        registry.enableSimpleBroker("/topic", "/queue");
        registry.setApplicationDestinationPrefixes("/app");
        // user destination prefix (default is "/user") — explicit for clarity
        registry.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Allow clients to set a username via query param (e.g. ?username=alice)
        registry.addEndpoint("/websocket")
                .setAllowedOriginPatterns("*")
                .setAllowedOrigins("http://localhost:5173", "http://127.0.0.1:5173")
                 .setHandshakeHandler(new com.example.demo.config.UserHandshakeHandler())
                .withSockJS();
    }
}