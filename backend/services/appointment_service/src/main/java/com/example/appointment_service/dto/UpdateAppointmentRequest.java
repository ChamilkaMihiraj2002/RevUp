package com.example.appointment_service.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UpdateAppointmentRequest {
    private Long technicianId;
    private LocalDateTime scheduledStart;
    private LocalDateTime scheduledEnd;
    private String status;
}
