package com.example.demo.controller;

import com.example.demo.dto.EmailRequest;
import com.example.demo.service.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/email")
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendEmail(@RequestBody EmailRequest request) {
        String result = emailService.sendEmail(request);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/test")
    public ResponseEntity<String> testEmail() {
        EmailRequest request = new EmailRequest();
        request.setTo("aa7211017@gmail.com"); // The recipient email
        request.setSubject("Test Email from Notification Service");
        request.setDescription("This is a test email from your notification service. If you receive this, the email configuration is working correctly!");
        String result = emailService.sendEmail(request);
        return ResponseEntity.ok(result);
    }
}
