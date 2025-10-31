package com.revup.notification_service.configs;

import com.revup.notification_service.websocket.NotificationWebSocketHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class WebSocketConfig {
    
    private final NotificationWebSocketHandler notificationWebSocketHandler;
    
    @Bean
    public HandlerMapping webSocketHandlerMapping() {
        Map<String, Object> map = new HashMap<>();
        map.put("/ws/notifications", notificationWebSocketHandler);
        
        SimpleUrlHandlerMapping handlerMapping = new SimpleUrlHandlerMapping();
        handlerMapping.setOrder(1);
        handlerMapping.setUrlMap(map);
        
        // Configure CORS
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("http://localhost:3000");
        corsConfiguration.addAllowedOrigin("http://localhost:5173");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.setAllowCredentials(true);
        
        Map<String, CorsConfiguration> corsConfigurationMap = new HashMap<>();
        corsConfigurationMap.put("/ws/**", corsConfiguration);
        handlerMapping.setCorsConfigurations(corsConfigurationMap);
        
        return handlerMapping;
    }
    
    @Bean
    public WebSocketHandlerAdapter handlerAdapter() {
        return new WebSocketHandlerAdapter();
    }
}
