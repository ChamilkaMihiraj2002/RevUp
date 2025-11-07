package com.example.demo.controller;

import com.example.demo.model.NotificationRequest;
import com.example.demo.service.NotificationServiceImpl;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
public class RealTimeNotifiController {

    private final NotificationServiceImpl notificationService;

    public RealTimeNotifiController(NotificationServiceImpl notificationService) {
        this.notificationService = notificationService;
    }

    /**
     * Accepts a NotificationRequest. Persist and send it using NotificationService.
     */
    @MessageMapping("/sendMessage")
    public void sendMessage(@Payload NotificationRequest request) {
        if (request == null) return;
        notificationService.saveAndSend(request);
    }
}