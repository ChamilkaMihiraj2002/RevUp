package com.revup.vehicle_service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleResponse {
    private Long id;
    private Long customerId;
    private String make;
    private String model;
    private Integer year;
    private String vin;
    private String licensePlate;
    private String color;
    private Long mileage;
    private String fuelType;
    private String transmissionType;
    private String engineSize;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
