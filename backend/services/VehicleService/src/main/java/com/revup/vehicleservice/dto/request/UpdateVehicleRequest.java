package com.revup.vehicleservice.dto.request;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for updating an existing vehicle
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateVehicleRequest {

    private String model;

    private String registrationNo;

    @Min(value = 1900, message = "Year must be 1900 or later")
    private Integer year;

    private String color;

    private String vehicleType;
}
