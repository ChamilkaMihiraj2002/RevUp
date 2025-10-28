package com.revup.time_tracking_service.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data // Lombok annotation for getters, setters, toString, etc.
public class TimeLogDTO {
    private Long timelogId;
    private Long appointmentServiceId;
    private Long userId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String description;
    private LocalDate logDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}