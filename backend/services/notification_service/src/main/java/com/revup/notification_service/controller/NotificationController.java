package com.revup.notification_service.controller;

import com.revup.notification_service.dto.CreateNotificationRequest;
import com.revup.notification_service.dto.MarkReadRequest;
import com.revup.notification_service.dto.NotificationResponse;
import com.revup.notification_service.enums.NotificationType;
import com.revup.notification_service.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Tag(name = "Notification Management", description = "APIs for real-time notification management")
public class NotificationController {
    
    private final NotificationService notificationService;
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create new notification")
    public Mono<NotificationResponse> createNotification(@Valid @RequestBody CreateNotificationRequest request) {
        return notificationService.createNotification(request);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get notification by ID")
    public Mono<NotificationResponse> getNotificationById(@PathVariable Long id) {
        return notificationService.getNotificationById(id);
    }
    
    @GetMapping
    @Operation(summary = "Get all notifications")
    public Flux<NotificationResponse> getAllNotifications() {
        return notificationService.getAllNotifications();
    }
    
    @GetMapping("/user/{userId}")
    @Operation(summary = "Get notifications by user")
    public Flux<NotificationResponse> getNotificationsByUser(@PathVariable Long userId) {
        return notificationService.getNotificationsByUser(userId);
    }
    
    @GetMapping("/user/{userId}/unread")
    @Operation(summary = "Get unread notifications by user")
    public Flux<NotificationResponse> getUnreadNotificationsByUser(@PathVariable Long userId) {
        return notificationService.getUnreadNotificationsByUser(userId);
    }
    
    @GetMapping("/user/{userId}/unread-count")
    @Operation(summary = "Get unread notification count")
    public Mono<Long> getUnreadCount(@PathVariable Long userId) {
        return notificationService.getUnreadCount(userId);
    }
    
    @GetMapping("/type/{type}")
    @Operation(summary = "Get notifications by type")
    public Flux<NotificationResponse> getNotificationsByType(@PathVariable NotificationType type) {
        return notificationService.getNotificationsByType(type);
    }
    
    @PatchMapping("/{id}/read")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Mark notification as read")
    public Mono<Void> markAsRead(@PathVariable Long id) {
        return notificationService.markAsRead(id);
    }
    
    @PatchMapping("/read")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Mark multiple notifications as read")
    public Mono<Void> markMultipleAsRead(@RequestBody MarkReadRequest request) {
        return notificationService.markMultipleAsRead(request.getNotificationIds());
    }
    
    @PatchMapping("/user/{userId}/read-all")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Mark all notifications as read for user")
    public Mono<Void> markAllAsReadForUser(@PathVariable Long userId) {
        return notificationService.markAllAsReadForUser(userId);
    }
    
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete notification")
    public Mono<Void> deleteNotification(@PathVariable Long id) {
        return notificationService.deleteNotification(id);
    }
    
    @DeleteMapping("/user/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete all notifications for user")
    public Mono<Void> deleteAllForUser(@PathVariable Long userId) {
        return notificationService.deleteAllForUser(userId);
    }
}
