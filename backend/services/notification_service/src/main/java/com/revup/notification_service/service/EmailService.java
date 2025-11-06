package com.revup.notification_service.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class EmailService {

    private static final Logger LOGGER = Logger.getLogger(EmailService.class.getName());

    private final JavaMailSender mailSender;
    
    @Value("${spring.mail.username:}")
    private String fromEmail;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Send an email asynchronously
     */
    @Async
    public void sendEmail(String to, String subject, String body) {
        if (to == null || to.trim().isEmpty()) {
            LOGGER.warning("Cannot send email: recipient address is empty");
            return;
        }

        if (fromEmail == null || fromEmail.trim().isEmpty()) {
            LOGGER.warning("Email service not configured: spring.mail.username is not set");
            return;
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            
            mailSender.send(message);
            LOGGER.info("Email sent successfully to: " + to);
        } catch (MailException e) {
            LOGGER.log(Level.SEVERE, "Failed to send email to: " + to, e);
        }
    }

    /**
     * Send appointment confirmation email
     */
    @Async
    public void sendAppointmentConfirmation(String toEmail, Long appointmentId, String scheduledTime) {
        String subject = "Appointment Confirmation - RevUp Auto Service";
        String body = String.format(
            "Dear Customer,\n\n" +
            "Your appointment (ID: %d) has been confirmed for %s.\n\n" +
            "We look forward to serving you.\n\n" +
            "Best regards,\n" +
            "RevUp Auto Service Team",
            appointmentId, scheduledTime
        );
        sendEmail(toEmail, subject, body);
    }

    /**
     * Send appointment status update email
     */
    @Async
    public void sendAppointmentStatusUpdate(String toEmail, Long appointmentId, String status) {
        String subject = "Appointment Status Update - RevUp Auto Service";
        String body = String.format(
            "Dear Customer,\n\n" +
            "Your appointment (ID: %d) status has been updated to: %s.\n\n" +
            "You can track your appointment progress in real-time through our portal.\n\n" +
            "Best regards,\n" +
            "RevUp Auto Service Team",
            appointmentId, status
        );
        sendEmail(toEmail, subject, body);
    }

    /**
     * Send time log progress update email
     */
    @Async
    public void sendProgressUpdate(String toEmail, Long appointmentId, String description) {
        String subject = "Service Progress Update - RevUp Auto Service";
        String body = String.format(
            "Dear Customer,\n\n" +
            "We have an update on your appointment (ID: %d):\n\n" +
            "%s\n\n" +
            "You can view detailed progress in your customer dashboard.\n\n" +
            "Best regards,\n" +
            "RevUp Auto Service Team",
            appointmentId, description
        );
        sendEmail(toEmail, subject, body);
    }
}
