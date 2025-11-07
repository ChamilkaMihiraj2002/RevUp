package com.example.demo.dto;

import lombok.Getter;

@Getter
public class EmailRequest {
    private String to;
    private String subject;
    private String description;

    public EmailRequest() {}

    public EmailRequest(String to, String subject, String description) {
        this.to = to;
        this.subject = subject;
        this.description = description;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
