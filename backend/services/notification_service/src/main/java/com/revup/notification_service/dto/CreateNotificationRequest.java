package com.revup.notification_service.dto;

import com.revup.notification_service.enums.NotificationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateNotificationRequest {
    
    @NotNull(message = "User ID is required")
    private Long userId;
    
    @NotNull(message = "Notification type is required")
    private NotificationType type;
    
    private String title;
    
    @NotBlank(message = "Message is required")
    private String message;
    
    private String relatedEntityType;
    
    private Long relatedEntityId;
}
