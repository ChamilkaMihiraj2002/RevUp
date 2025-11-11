package com.example.demo.service;

import com.example.demo.dto.EmailRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class EmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;
    private final String fromEmail;

    public EmailService(JavaMailSender mailSender, @Value("${spring.mail.username}") String fromEmail) {
        this.mailSender = mailSender;
        this.fromEmail = fromEmail;
        logger.info("EmailService initialized with sender email: {}", fromEmail);
    }

    public Mono<String> sendEmail(EmailRequest request) {
        return Mono.fromCallable(() -> {
            logger.info("Attempting to send email to: {}", request.getTo());
            try {
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true);

                helper.setFrom(fromEmail);
                helper.setTo(request.getTo());
                helper.setSubject(request.getSubject());
                helper.setText(request.getDescription(), true); // true indicates HTML content

                mailSender.send(message);
                logger.info("Email sent successfully to: {}", request.getTo());
                return "Email sent successfully to: " + request.getTo();
            } catch (MailAuthenticationException e) {
                logger.error("Gmail authentication failed. Please check your credentials and ensure you're using an App Password", e);
                throw new RuntimeException("Email authentication failed. Please ensure you're using a valid Gmail App Password");
            } catch (MessagingException e) {
                logger.error("Failed to prepare email message: {}", e.getMessage(), e);
                throw new RuntimeException("Failed to prepare email: " + e.getMessage());
            } catch (MailException e) {
                logger.error("Failed to send email: {}", e.getMessage(), e);
                throw new RuntimeException("Failed to send email: " + e.getMessage());
            }
        });
    }
}
