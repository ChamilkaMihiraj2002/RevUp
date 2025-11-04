package com.example.appointment_service.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CreateAppointmentRequest {
    private Long customerId;
    private Long vehicleId;
    private Long technicianId;
    private LocalDateTime scheduledStart;
    private LocalDateTime scheduledEnd;
}
