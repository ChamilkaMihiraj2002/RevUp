package com.revup.notification_service.repository;

import com.revup.notification_service.entity.Notification;
import com.revup.notification_service.enums.NotificationType;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.List;

@Repository
public interface NotificationRepository extends R2dbcRepository<Notification, Long> {
    
    Flux<Notification> findByUserId(Long userId);
    
    Flux<Notification> findByUserIdAndIsRead(Long userId, Boolean isRead);
    
    Flux<Notification> findByUserIdOrderByCreatedAtDesc(Long userId);
    
    Flux<Notification> findByType(NotificationType type);
    
    @Query("SELECT * FROM notification_schema.notifications WHERE user_id = :userId AND is_read = false ORDER BY created_at DESC")
    Flux<Notification> findUnreadByUserIdOrderByCreatedAtDesc(Long userId);
    
    @Query("SELECT COUNT(*) FROM notification_schema.notifications WHERE user_id = :userId AND is_read = false")
    Mono<Long> countUnreadByUserId(Long userId);
    
    @Query("UPDATE notification_schema.notifications SET is_read = true WHERE id IN (:ids)")
    Mono<Void> markAsReadByIds(List<Long> ids);
    
    @Query("UPDATE notification_schema.notifications SET is_read = true WHERE user_id = :userId")
    Mono<Void> markAllAsReadByUserId(Long userId);
}
