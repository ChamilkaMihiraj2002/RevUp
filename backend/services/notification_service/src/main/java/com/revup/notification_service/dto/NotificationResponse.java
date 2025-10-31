package com.revup.notification_service.dto;

import com.revup.notification_service.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse {
    private Long id;
    private Long userId;
    private NotificationType type;
    private String title;
    private String message;
    private Boolean isRead;
    private String relatedEntityType;
    private Long relatedEntityId;
    private LocalDateTime createdAt;
}
