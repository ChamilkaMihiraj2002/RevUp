package com.revup.notification_service.mapper;

import com.revup.notification_service.dto.CreateNotificationRequest;
import com.revup.notification_service.dto.NotificationResponse;
import com.revup.notification_service.entity.Notification;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-10-31T12:54:56+0530",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.44.0.v20251001-1143, environment: Java 21.0.8 (Eclipse Adoptium)"
)
@Component
public class NotificationMapperImpl implements NotificationMapper {

    @Override
    public Notification toEntity(CreateNotificationRequest request) {
        if ( request == null ) {
            return null;
        }

        Notification notification = new Notification();

        notification.setMessage( request.getMessage() );
        notification.setRelatedEntityId( request.getRelatedEntityId() );
        notification.setRelatedEntityType( request.getRelatedEntityType() );
        notification.setTitle( request.getTitle() );
        notification.setType( request.getType() );
        notification.setUserId( request.getUserId() );

        return notification;
    }

    @Override
    public NotificationResponse toResponse(Notification notification) {
        if ( notification == null ) {
            return null;
        }

        NotificationResponse notificationResponse = new NotificationResponse();

        notificationResponse.setCreatedAt( notification.getCreatedAt() );
        notificationResponse.setId( notification.getId() );
        notificationResponse.setIsRead( notification.getIsRead() );
        notificationResponse.setMessage( notification.getMessage() );
        notificationResponse.setRelatedEntityId( notification.getRelatedEntityId() );
        notificationResponse.setRelatedEntityType( notification.getRelatedEntityType() );
        notificationResponse.setTitle( notification.getTitle() );
        notificationResponse.setType( notification.getType() );
        notificationResponse.setUserId( notification.getUserId() );

        return notificationResponse;
    }
}
