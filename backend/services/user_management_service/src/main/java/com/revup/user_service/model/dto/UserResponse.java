package com.revup.user_service.model.dto;

import com.revup.user_service.model.enums.UserRole;
import com.revup.user_service.model.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String firebaseUid;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private UserRole role;
    private UserStatus status;
    private String profileImageUrl;
    private String address;
    private String city;
    private String state;
    private String zipCode;
    private String country;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
