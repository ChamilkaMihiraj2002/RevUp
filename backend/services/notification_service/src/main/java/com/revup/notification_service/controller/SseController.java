package com.revup.notification_service.controller;

import com.revup.notification_service.dto.NotificationResponse;
import com.revup.notification_service.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;

@RestController
@RequestMapping("/api/notifications/sse")
@RequiredArgsConstructor
@Tag(name = "Server-Sent Events", description = "SSE endpoints for real-time notification streaming")
public class SseController {
    
    private final NotificationService notificationService;
    
    @GetMapping(value = "/user/{userId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(summary = "Stream notifications for user via SSE")
    public Flux<NotificationResponse> streamUserNotifications(@PathVariable Long userId) {
        return notificationService.getUnreadNotificationsByUser(userId)
                .delayElements(Duration.ofSeconds(1)); // Throttle to prevent overwhelming client
    }
    
    @GetMapping(value = "/all", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(summary = "Stream all notifications via SSE")
    public Flux<NotificationResponse> streamAllNotifications() {
        return notificationService.getAllNotifications()
                .delayElements(Duration.ofSeconds(1));
    }
}
