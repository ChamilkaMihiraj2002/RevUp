package com.revup.vehicleservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for vehicle response
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleResponse {

    private Long vehicleId;
    private String model;
    private String registrationNo;
    private Integer year;
    private String color;
    private String vehicleType;
    private Long userId;
}
