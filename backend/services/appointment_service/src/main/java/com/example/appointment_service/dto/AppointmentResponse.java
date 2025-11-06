package com.example.appointment_service.dto;

import com.example.appointment_service.enums.AppointmentStatus;
import lombok.Data;
import java.time.OffsetDateTime;

@Data
public class AppointmentResponse {
    private Long appointmentId;
    private Long customerId;
    private Long vehicleId;
    private Long technicianId;
    private AppointmentStatus status;
    private OffsetDateTime scheduledStart;
    private OffsetDateTime scheduledEnd;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
