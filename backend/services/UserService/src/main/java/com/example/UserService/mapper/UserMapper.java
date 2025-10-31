package com.example.UserService.mapper;

import com.example.UserService.dto.request.CreateUserRequest;
import com.example.UserService.dto.request.UpdateUserRequest;
import com.example.UserService.dto.response.UserResponse;
import com.example.UserService.dto.response.VehicleResponse;
import com.example.UserService.entity.User;
import com.example.UserService.entity.Vehicle;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper for converting between User entities and DTOs
 */
@Component
public class UserMapper {

    /**
     * Convert CreateUserRequest DTO to User entity
     */
    public User toEntity(CreateUserRequest request) {
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setAddress(request.getAddress());
        user.setRole(request.getRole());
        return user;
    }

    /**
     * Update User entity from UpdateUserRequest DTO
     */
    public void updateEntityFromDto(User user, UpdateUserRequest request) {
        if (request.getName() != null) {
            user.setName(request.getName());
        }
        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }
        if (request.getPhone() != null) {
            user.setPhone(request.getPhone());
        }
        if (request.getAddress() != null) {
            user.setAddress(request.getAddress());
        }
        if (request.getRole() != null) {
            user.setRole(request.getRole());
        }
    }

    /**
     * Convert User entity to UserResponse DTO
     */
    public UserResponse toResponse(User user) {
        List<VehicleResponse> vehicleResponses = user.getVehicles() != null
                ? user.getVehicles().stream()
                        .map(this::toVehicleResponse)
                        .collect(Collectors.toList())
                : Collections.emptyList();

        return UserResponse.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .address(user.getAddress())
                .role(user.getRole())
                .vehicles(vehicleResponses)
                .build();
    }

    /**
     * Convert Vehicle entity to VehicleResponse DTO
     */
    private VehicleResponse toVehicleResponse(Vehicle vehicle) {
        return VehicleResponse.builder()
                .vehicleId(vehicle.getVehicleId())
                .model(vehicle.getModel())
                .registrationNo(vehicle.getRegistrationNo())
                .year(vehicle.getYear())
                .build();
    }
}
