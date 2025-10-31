package com.revup.appointment_service.dto;

import com.revup.appointment_service.model.AppointmentStatus;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAppointmentRequest {

    @Future(message = "Scheduled date must be in the future")
    private LocalDateTime scheduledDate;

    private AppointmentStatus status;

    @Min(value = 15, message = "Estimated duration must be at least 15 minutes")
    private Integer estimatedDuration;

    private Long employeeId;

    private String notes;
}
