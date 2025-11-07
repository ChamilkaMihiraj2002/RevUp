package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class NotificationDocument {
    private String id;
    private String to;
    private String message;
    private Instant createdAt;
    private boolean read;
}