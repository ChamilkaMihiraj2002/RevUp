package com.example.appointment_service.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CreateAppointmentRequest {
    
    @NotNull(message = "Customer ID is required")
    private Long customerId;
    
    @NotNull(message = "Vehicle ID is required")
    private Long vehicleId;
    
    private Long technicianId;
    
    @NotNull(message = "Scheduled start time is required")
    @Future(message = "Scheduled start time must be in the future")
    private LocalDateTime scheduledStart;
    
    @NotNull(message = "Scheduled end time is required")
    @Future(message = "Scheduled end time must be in the future")
    private LocalDateTime scheduledEnd;
}
