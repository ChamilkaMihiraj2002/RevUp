package com.example.demo.service;

import com.example.demo.model.NotificationRequest;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationServiceImpl {

    private final SimpMessagingTemplate messagingTemplate;

    public NotificationServiceImpl(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * Save and send notification. Since we're not using DB, we just send it via WebSocket.
     */
    public void saveAndSend(NotificationRequest request) {
        if (request == null) return;

        // If 'to' is specified, send to that specific user
        if (request.getTo() != null && !request.getTo().isEmpty()) {
            messagingTemplate.convertAndSendToUser(
                request.getTo(),
                "/queue/notifications",
                request.getMessage()
            );
        } else {
            // Otherwise broadcast to all
            messagingTemplate.convertAndSend("/topic/notifications", request.getMessage());
        }
    }

    /**
     * Find notifications by user. Since we're not saving to DB, return empty list.
     */
    public List<com.example.demo.model.NotificationDocument> findByUser(String userId) {
        // Not storing in database, return empty list
        return new ArrayList<>();
    }
}
