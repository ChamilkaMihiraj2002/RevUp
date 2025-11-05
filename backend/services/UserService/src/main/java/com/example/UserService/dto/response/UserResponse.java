package com.example.UserService.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import com.example.UserService.Enum.Role;

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
    private Role role;
    private String firebaseUID;
    
    private List<Long> vehicleIds;
}
