package com.revup.notification_service.dto;

import java.time.LocalDateTime;

public class TimeLogEventDTO {
    private String eventType; // TIMELOG_CREATED, TIMELOG_UPDATED
    private Long timeLogId;
    private Long appointmentServiceId;
    private Long appointmentId;
    private Long customerId;
    private Long employeeId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String description;
    private LocalDateTime logDate;
    private LocalDateTime eventTimestamp;

    public TimeLogEventDTO() {}

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public Long getTimeLogId() { return timeLogId; }
    public void setTimeLogId(Long timeLogId) { this.timeLogId = timeLogId; }

    public Long getAppointmentServiceId() { return appointmentServiceId; }
    public void setAppointmentServiceId(Long appointmentServiceId) { this.appointmentServiceId = appointmentServiceId; }

    public Long getAppointmentId() { return appointmentId; }
    public void setAppointmentId(Long appointmentId) { this.appointmentId = appointmentId; }

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public Long getEmployeeId() { return employeeId; }
    public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getLogDate() { return logDate; }
    public void setLogDate(LocalDateTime logDate) { this.logDate = logDate; }

    public LocalDateTime getEventTimestamp() { return eventTimestamp; }
    public void setEventTimestamp(LocalDateTime eventTimestamp) { this.eventTimestamp = eventTimestamp; }
}
