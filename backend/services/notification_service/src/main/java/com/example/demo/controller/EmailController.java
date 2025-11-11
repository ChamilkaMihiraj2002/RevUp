package com.example.demo.controller;

import com.example.demo.dto.EmailRequest;
import com.example.demo.service.EmailService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/email")
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/send")
    public Mono<String> sendEmail(@RequestBody EmailRequest request) {
        return emailService.sendEmail(request);
    }

    @GetMapping("/test")
    public Mono<String> testEmail() {
        EmailRequest request = new EmailRequest();
        request.setTo("aa7211017@gmail.com"); // The recipient email
        request.setSubject("Test Email from Notification Service");
        request.setDescription("This is a test email from your notification service. If you receive this, the email configuration is working correctly!");
        return emailService.sendEmail(request);
    }
}
