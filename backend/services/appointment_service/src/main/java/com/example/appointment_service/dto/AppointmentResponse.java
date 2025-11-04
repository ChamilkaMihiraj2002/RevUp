package com.example.appointment_service.dto;

import lombok.Data;
import java.time.OffsetDateTime;

@Data
public class AppointmentResponse {
    private Long appointmentId;
    private Long customerId;
    private Long vehicleId;
    private Long technicianId;
    private String status;
    private OffsetDateTime scheduledStart;
    private OffsetDateTime scheduledEnd;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
