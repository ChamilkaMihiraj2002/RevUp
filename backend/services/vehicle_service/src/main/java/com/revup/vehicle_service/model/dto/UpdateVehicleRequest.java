package com.revup.vehicle_service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateVehicleRequest {
    private String color;
    private Long mileage;
    private String fuelType;
    private String transmissionType;
    private String engineSize;
    private String notes;
    private String licensePlate;
}
