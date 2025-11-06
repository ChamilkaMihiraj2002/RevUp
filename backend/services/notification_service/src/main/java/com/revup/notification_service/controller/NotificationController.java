package com.revup.notification_service.controller;

import com.revup.notification_service.dto.NotificationMessage;
import com.revup.notification_service.entity.Notification;
import com.revup.notification_service.service.NotificationService;
import com.revup.notification_service.rabbit.RabbitConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final RabbitTemplate rabbitTemplate;
    private final NotificationService notificationService;

    public NotificationController(RabbitTemplate rabbitTemplate, NotificationService notificationService) {
        this.rabbitTemplate = rabbitTemplate;
        this.notificationService = notificationService;
    }

    // Publish a sample event to the exchange (for testing integration)
    @PostMapping("/publish")
    public ResponseEntity<String> publish(@RequestBody NotificationMessage msg) {
        rabbitTemplate.convertAndSend(RabbitConfig.EVENTS_EXCHANGE, "appointments.updated", msg);
        return ResponseEntity.ok("published");
    }

    // Save an in-app notification and optionally push via websocket
    @PostMapping("/save")
    public ResponseEntity<Notification> save(@RequestBody Notification notif) {
        Notification saved = notificationService.save(notif);
        // push to user
        NotificationMessage msg = new NotificationMessage(notif.getType(), null, notif.getUserId(), "New Notification", notif.getMessage());
        notificationService.sendToUser(notif.getUserId(), msg);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Notification>> getForUser(@PathVariable Long userId) {
        return ResponseEntity.ok(notificationService.getForUser(userId));
    }
}

