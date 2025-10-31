package com.revup.appointment_service.dto;

import com.revup.appointment_service.model.AppointmentStatus;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAppointmentRequest {

    @NotNull(message = "Customer ID is required")
    private Long customerId;

    @NotNull(message = "Vehicle ID is required")
    private Long vehicleId;

    @NotBlank(message = "Service type is required")
    @Size(max = 100, message = "Service type must not exceed 100 characters")
    private String serviceType;

    @NotNull(message = "Scheduled date is required")
    @Future(message = "Scheduled date must be in the future")
    private LocalDateTime scheduledDate;

    @Min(value = 15, message = "Estimated duration must be at least 15 minutes")
    private Integer estimatedDuration;

    private Long employeeId;

    private String notes;
}
