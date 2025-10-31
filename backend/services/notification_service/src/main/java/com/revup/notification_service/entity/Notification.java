package com.revup.notification_service.entity;

import com.revup.notification_service.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "notifications", schema = "notification_schema")
public class Notification {
    
    @Id
    private Long id;
    
    @Column("user_id")
    private Long userId;
    
    @Column("type")
    private NotificationType type;
    
    @Column("title")
    private String title;
    
    @Column("message")
    private String message;
    
    @Column("is_read")
    private Boolean isRead;
    
    @Column("related_entity_type")
    private String relatedEntityType;
    
    @Column("related_entity_id")
    private Long relatedEntityId;
    
    @Column("created_at")
    private LocalDateTime createdAt;
}
