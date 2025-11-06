package com.revup.notification_service.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId; // The user who will receive this

    @Column(nullable = false)
    private String type; // e.g., "APPOINTMENT_CONFIRMED", "PROGRESS_UPDATE"

    @Column(nullable = false)
    private String message;

    private boolean isRead = false;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();


}