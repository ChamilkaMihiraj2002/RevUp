package com.revup.vehicleservice.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for creating a new vehicle
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateVehicleRequest {

    @NotBlank(message = "Model is required")
    private String model;

    @NotBlank(message = "Registration number is required")
    private String registrationNo;

    @NotNull(message = "Year is required")
    @Min(value = 1900, message = "Year must be 1900 or later")
    private Integer year;

    private String color;

    @NotBlank(message = "Vehicle type is required")
    private String vehicleType;

    @NotNull(message = "User ID is required")
    private Long userId;
}
