package com.revup.notification_service.service;

import com.revup.notification_service.dto.NotificationMessage;
import com.revup.notification_service.model.Notification;
import com.revup.notification_service.repository.NotificationRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationRepository repository;

    public NotificationService(SimpMessagingTemplate messagingTemplate, NotificationRepository repository) {
        this.messagingTemplate = messagingTemplate;
        this.repository = repository;
    }

    public void sendToAppointment(Long appointmentId, NotificationMessage message) {
        if (appointmentId == null) return;
        String dest = "/topic/appointments." + appointmentId;
        messagingTemplate.convertAndSend(dest, message);
    }

    public void sendToUser(Long userId, NotificationMessage message) {
        if (userId == null) return;
        messagingTemplate.convertAndSendToUser(String.valueOf(userId), "/queue/notifications", message);
    }

    @Transactional
    public Notification save(Notification notif) {
        return repository.save(notif);
    }

    public List<Notification> getForUser(Long userId) {
        return repository.findByUserIdOrderByCreatedAtDesc(userId);
    }
}

