package com.revup.time_tracking_service.dto.request;

import lombok.Data;
import java.time.LocalDate;
import java.time.Instant;

/**
 * DTO for client requests (POST/PUT) to create or update a time log.
 * It only contains fields the client is expected to provide.
 */
@Data
public class TimeLogRequest {

    private Long appointmentServiceId;

    private Long userId;

    private Instant startTime;

    private Instant endTime;

    private Long actualMinutes;

    private String description;

    private LocalDate logDate;
}