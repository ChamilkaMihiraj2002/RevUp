package com.example.UserService.mapper;

import com.example.UserService.dto.request.CreateUserRequest;
import com.example.UserService.dto.request.UpdateUserRequest;
import com.example.UserService.dto.response.UserResponse;
import com.example.UserService.entity.User;
import com.example.UserService.dto.UserDto;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

/**
 * Mapper for converting between User entities and DTOs
 */
@Component
public class UserMapper {
    private final WebClient webClient;

    public UserMapper(WebClient.Builder webClientBuilder) {
        // Replace the baseUrl with your Vehicle service URL
        this.webClient = webClientBuilder.baseUrl("http://localhost:8084/api/v1/vehicles").build();
    }
    /**
     * Convert User entity to UserDto with only vehicleIds
     */
    public UserDto toDto(User user) {
        UserDto dto = new UserDto();
        dto.setUserId(user.getUserId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setAddress(user.getAddress());
        dto.setRole(user.getRole());
        dto.setVehicleIds(user.getVehicleIds() != null ? user.getVehicleIds() : new ArrayList<>());
        return dto;
    }

    /**
     * Convert CreateUserRequest DTO to User entity
     */
    public User toEntity(CreateUserRequest request) {
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setAddress(request.getAddress());
        if (request.getRole() != null) {
            user.setRole(com.example.UserService.Enum.Role.valueOf(request.getRole().toUpperCase()));
        }
        user.setVehicleIds(new ArrayList<>());
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
            user.setRole(com.example.UserService.Enum.Role.valueOf(request.getRole().toUpperCase()));
        }
    }

    /**
     * Convert User entity to UserResponse DTO
     */
    public UserResponse toResponse(User user) {
        return UserResponse.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .address(user.getAddress())
                .role(user.getRole())
                .vehicleIds(user.getVehicleIds() != null ? user.getVehicleIds() : Collections.emptyList())
                .build();
    }
}

