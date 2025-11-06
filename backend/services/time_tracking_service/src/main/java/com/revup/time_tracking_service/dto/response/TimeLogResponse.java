package com.revup.time_tracking_service.dto.response;

import lombok.Data;
import java.time.LocalDate;
import java.time.Instant;

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

    private Instant startTime;

    private Instant endTime;

    private Long actualMinutes;

    private String description;

    private LocalDate logDate;

    private Instant createdAt;

    private Instant updatedAt;
}