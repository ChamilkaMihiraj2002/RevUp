package com.example.appointment_service.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateServiceTypeRequest {
    private String name;
    private String description;
    private Integer baseDurationMinutes;
    private Double basePrice;
}
