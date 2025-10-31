package com.revup.vehicle_service.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateVehicleRequest {
    
    @NotNull(message = "Customer ID is required")
    private Long customerId;
    
    @NotBlank(message = "Make is required")
    private String make;
    
    @NotBlank(message = "Model is required")
    private String model;
    
    @NotNull(message = "Year is required")
    private Integer year;
    
    @NotBlank(message = "VIN is required")
    private String vin;
    
    @NotBlank(message = "License plate is required")
    private String licensePlate;
    
    private String color;
    private Long mileage;
    private String fuelType;
    private String transmissionType;
    private String engineSize;
    private String notes;
}
