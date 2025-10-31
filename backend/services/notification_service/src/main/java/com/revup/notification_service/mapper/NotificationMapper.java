package com.revup.notification_service.mapper;

import com.revup.notification_service.dto.CreateNotificationRequest;
import com.revup.notification_service.dto.NotificationResponse;
import com.revup.notification_service.entity.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface NotificationMapper {
    
    Notification toEntity(CreateNotificationRequest request);
    
    NotificationResponse toResponse(Notification notification);
}
