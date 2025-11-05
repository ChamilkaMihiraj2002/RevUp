package com.example.UserService.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for user response with all details
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private Long userId;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String role;
    
    // In microservices, we only store vehicle IDs
    // To get full vehicle details, the client should call the VehicleService
    private List<Long> vehicleIds;
}
