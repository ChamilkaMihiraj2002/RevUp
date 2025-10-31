package com.revup.time_tracking_service.dto.response;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO for server responses (GET).
 * It represents the full TimeLog entity as the client should see it,
 * including server-generated fields like IDs and timestamps.
 */
@Data
public class TimeLogResponse {

    private Long timelogId;

    private Long appointmentServiceId;

    private Long userId;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Long actualMinutes;

    private String description;

    private LocalDate logDate;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}