package com.revup.notification_service.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class NotificationConsumer {

    private static final Logger LOGGER = Logger.getLogger(NotificationConsumer.class.getName());


    @RabbitListener(queues = "notification.queue")
    public void handleNotification(String message) {

        LOGGER.info("=========================================");
        LOGGER.info("Received notification: " + message);
        LOGGER.info("=========================================");

    }
}