package com.example.appointment_service.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateAppointmentServiceRequest {
    private Integer actualMinutes;
    private String status;
    private Integer quantity;
    private Integer estimatedMinutes;
    private Long appointmentId;
    private Long serviceTypeId;
}
