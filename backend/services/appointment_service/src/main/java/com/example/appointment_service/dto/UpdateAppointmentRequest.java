package com.example.appointment_service.dto;

import com.example.appointment_service.enums.AppointmentStatus;
import jakarta.validation.constraints.Future;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UpdateAppointmentRequest {
    
    private Long technicianId;
    
    @Future(message = "Scheduled start time must be in the future")
    private LocalDateTime scheduledStart;
    
    @Future(message = "Scheduled end time must be in the future")
    private LocalDateTime scheduledEnd;
    
    private AppointmentStatus status;
}
