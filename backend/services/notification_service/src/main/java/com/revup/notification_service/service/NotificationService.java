package com.revup.notification_service.service;

import com.revup.notification_service.dto.CreateNotificationRequest;
import com.revup.notification_service.dto.NotificationResponse;
import com.revup.notification_service.entity.Notification;
import com.revup.notification_service.enums.NotificationType;
import com.revup.notification_service.exception.ResourceNotFoundException;
import com.revup.notification_service.mapper.NotificationMapper;
import com.revup.notification_service.repository.NotificationRepository;
import com.revup.notification_service.websocket.NotificationWebSocketHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {
    
    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final NotificationWebSocketHandler webSocketHandler;
    
    public Mono<NotificationResponse> createNotification(CreateNotificationRequest request) {
        log.info("Creating notification for user: {}", request.getUserId());
        
        Notification notification = notificationMapper.toEntity(request);
        notification.setIsRead(false);
        notification.setCreatedAt(LocalDateTime.now());
        
        return notificationRepository.save(notification)
                .map(notificationMapper::toResponse)
                .doOnSuccess(response -> {
                    log.info("Notification created with id: {}", response.getId());
                    // Broadcast to WebSocket clients
                    webSocketHandler.broadcastNotification(response);
                });
    }
    
    public Mono<NotificationResponse> getNotificationById(Long id) {
        return notificationRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Notification not found with id: " + id)))
                .map(notificationMapper::toResponse);
    }
    
    public Flux<NotificationResponse> getAllNotifications() {
        return notificationRepository.findAll()
                .map(notificationMapper::toResponse);
    }
    
    public Flux<NotificationResponse> getNotificationsByUser(Long userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .map(notificationMapper::toResponse);
    }
    
    public Flux<NotificationResponse> getUnreadNotificationsByUser(Long userId) {
        return notificationRepository.findUnreadByUserIdOrderByCreatedAtDesc(userId)
                .map(notificationMapper::toResponse);
    }
    
    public Flux<NotificationResponse> getNotificationsByType(NotificationType type) {
        return notificationRepository.findByType(type)
                .map(notificationMapper::toResponse);
    }
    
    public Mono<Long> getUnreadCount(Long userId) {
        return notificationRepository.countUnreadByUserId(userId);
    }
    
    public Mono<Void> markAsRead(Long id) {
        return notificationRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Notification not found with id: " + id)))
                .flatMap(notification -> {
                    notification.setIsRead(true);
                    return notificationRepository.save(notification);
                })
                .then();
    }
    
    public Mono<Void> markMultipleAsRead(List<Long> ids) {
        return notificationRepository.markAsReadByIds(ids);
    }
    
    public Mono<Void> markAllAsReadForUser(Long userId) {
        return notificationRepository.markAllAsReadByUserId(userId);
    }
    
    public Mono<Void> deleteNotification(Long id) {
        return notificationRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Notification not found with id: " + id)))
                .flatMap(notification -> notificationRepository.deleteById(id));
    }
    
    public Mono<Void> deleteAllForUser(Long userId) {
        return notificationRepository.findByUserId(userId)
                .flatMap(notification -> notificationRepository.deleteById(notification.getId()))
                .then();
    }
}
