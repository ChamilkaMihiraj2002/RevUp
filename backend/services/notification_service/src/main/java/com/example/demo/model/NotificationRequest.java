package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class NotificationRequest {
    // 'to' is the target username (Principal.getName()) — if null/empty, the message will be broadcast
    private String to;
    private String message;
}