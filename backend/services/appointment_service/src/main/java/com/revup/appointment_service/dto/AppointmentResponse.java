package com.revup.appointment_service.dto;

import com.revup.appointment_service.model.AppointmentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentResponse {

    private Long id;
    private Long customerId;
    private Long vehicleId;
    private Long employeeId;
    private String serviceType;
    private LocalDateTime scheduledDate;
    private AppointmentStatus status;
    private Integer estimatedDuration;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
