package com.example.demo.controller;

import com.example.demo.model.NotificationRequest;
import com.example.demo.service.NotificationServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class NotifyRestController {

    private final NotificationServiceImpl notificationService;

    public NotifyRestController(NotificationServiceImpl notificationService) {
        this.notificationService = notificationService;
    }

    /**
     * Simple HTTP helper to send a notification. Use with Postman while testing frontend connections.
     * Body example: { "to": "user_...", "message": "Hello" }
     */
    @PostMapping("/notify")
    public ResponseEntity<Void> notify(@RequestBody NotificationRequest req) {
        if (req == null) return ResponseEntity.badRequest().build();

        notificationService.saveAndSend(req);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/notifications/{userId}")
    public ResponseEntity<java.util.List<com.example.demo.model.NotificationDocument>> getNotifications(@PathVariable("userId") String userId) {
        if (userId == null || userId.isEmpty()) return ResponseEntity.badRequest().build();
        java.util.List<com.example.demo.model.NotificationDocument> list = notificationService.findByUser(userId);
        return ResponseEntity.ok(list);
    }
}