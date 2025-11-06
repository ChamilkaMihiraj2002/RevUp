package com.revup.notification_service.dto;

import java.time.Instant;

public class NotificationMessage {
    private String type; // e.g., APPOINTMENT_UPDATED, TIMELOG_ADDED
    private Long appointmentId;
    private Long customerId;
    private String title;
    private String body;
    private Instant timestamp;

    public NotificationMessage() {}

    public NotificationMessage(String type, Long appointmentId, Long customerId, String title, String body) {
        this.type = type;
        this.appointmentId = appointmentId;
        this.customerId = customerId;
        this.title = title;
        this.body = body;
        this.timestamp = Instant.now();
    }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public Long getAppointmentId() { return appointmentId; }
    public void setAppointmentId(Long appointmentId) { this.appointmentId = appointmentId; }
    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }
    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }
}

