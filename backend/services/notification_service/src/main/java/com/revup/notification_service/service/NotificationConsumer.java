package com.revup.notification_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revup.notification_service.dto.AppointmentEventDTO;
import com.revup.notification_service.dto.NotificationMessage;
import com.revup.notification_service.dto.TimeLogEventDTO;
import com.revup.notification_service.model.Notification;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class NotificationConsumer {

    private static final Logger LOGGER = Logger.getLogger(NotificationConsumer.class.getName());

    private final NotificationService notificationService;
    private final EmailService emailService;
    private final ObjectMapper objectMapper;

    public NotificationConsumer(NotificationService notificationService, 
                               EmailService emailService,
                               ObjectMapper objectMapper) {
        this.notificationService = notificationService;
        this.emailService = emailService;
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = "notification.queue")
    public void handleNotification(String message) {
        LOGGER.info("=========================================");
        LOGGER.info("Received notification: " + message);
        LOGGER.info("=========================================");

        try {
            // Try to parse as different event types
            if (message.contains("APPOINTMENT") || message.contains("appointment")) {
                handleAppointmentEvent(message);
            } else if (message.contains("TIMELOG") || message.contains("timelog")) {
                handleTimeLogEvent(message);
            } else {
                LOGGER.warning("Unknown event type: " + message);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error processing notification", e);
        }
    }

    private void handleAppointmentEvent(String message) {
        try {
            AppointmentEventDTO event = objectMapper.readValue(message, AppointmentEventDTO.class);
            
            String notificationType = event.getEventType();
            String notificationMessage = buildAppointmentMessage(event);
            
            // Save notification for customer
            if (event.getCustomerId() != null) {
                Notification notification = new Notification();
                notification.setUserId(event.getCustomerId());
                notification.setType(notificationType);
                notification.setMessage(notificationMessage);
                notificationService.save(notification);
                
                // Send WebSocket notification to customer
                NotificationMessage wsMessage = new NotificationMessage(
                    notificationType,
                    event.getAppointmentId(),
                    event.getCustomerId(),
                    "Appointment Update",
                    notificationMessage
                );
                notificationService.sendToUser(event.getCustomerId(), wsMessage);
                notificationService.sendToAppointment(event.getAppointmentId(), wsMessage);
                
                // Send email notification (async)
                // Note: Email sending requires customer email, which should be fetched from User service
                // For now, we skip email as we don't have customer email in the event
            }
            
            // Notify technician if assigned
            if (event.getTechnicianId() != null) {
                Notification techNotification = new Notification();
                techNotification.setUserId(event.getTechnicianId());
                techNotification.setType(notificationType);
                techNotification.setMessage("New appointment assigned: " + notificationMessage);
                notificationService.save(techNotification);
                
                NotificationMessage wsMessage = new NotificationMessage(
                    notificationType,
                    event.getAppointmentId(),
                    event.getTechnicianId(),
                    "New Assignment",
                    "New appointment assigned to you"
                );
                notificationService.sendToUser(event.getTechnicianId(), wsMessage);
            }
            
            LOGGER.info("Appointment event processed: " + notificationType);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error handling appointment event", e);
        }
    }

    private void handleTimeLogEvent(String message) {
        try {
            TimeLogEventDTO event = objectMapper.readValue(message, TimeLogEventDTO.class);
            
            String notificationType = event.getEventType();
            String notificationMessage = buildTimeLogMessage(event);
            
            // Notify customer about progress update
            if (event.getCustomerId() != null) {
                Notification notification = new Notification();
                notification.setUserId(event.getCustomerId());
                notification.setType(notificationType);
                notification.setMessage(notificationMessage);
                notificationService.save(notification);
                
                // Send WebSocket notification
                NotificationMessage wsMessage = new NotificationMessage(
                    notificationType,
                    event.getAppointmentId(),
                    event.getCustomerId(),
                    "Service Progress Update",
                    notificationMessage
                );
                notificationService.sendToUser(event.getCustomerId(), wsMessage);
                if (event.getAppointmentId() != null) {
                    notificationService.sendToAppointment(event.getAppointmentId(), wsMessage);
                }
                
                // Send email notification (async)
                // Note: Email sending requires customer email
            }
            
            LOGGER.info("TimeLog event processed: " + notificationType);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error handling timelog event", e);
        }
    }

    private String buildAppointmentMessage(AppointmentEventDTO event) {
        return switch (event.getEventType()) {
            case "APPOINTMENT_CREATED" -> 
                "Your appointment has been created and is pending confirmation.";
            case "APPOINTMENT_CONFIRMED" -> 
                "Your appointment has been confirmed for " + event.getScheduledStart();
            case "APPOINTMENT_UPDATED" -> 
                "Your appointment details have been updated. New status: " + event.getStatus();
            case "APPOINTMENT_CANCELLED" -> 
                "Your appointment has been cancelled.";
            case "APPOINTMENT_IN_PROGRESS" -> 
                "Your vehicle service is now in progress.";
            case "APPOINTMENT_COMPLETED" -> 
                "Your vehicle service has been completed. Please review your invoice.";
            default -> 
                "Appointment status: " + event.getStatus();
        };
    }

    private String buildTimeLogMessage(TimeLogEventDTO event) {
        String description = event.getDescription() != null ? event.getDescription() : "Service in progress";
        return "Progress update: " + description;
    }
}