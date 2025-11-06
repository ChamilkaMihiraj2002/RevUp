package com.example.appointment_service.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SlotAvailabilityResponse {
    private boolean available;
    private int bookedCount;
    private int maxCapacity;
    private String message;
}
