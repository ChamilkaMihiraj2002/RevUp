package com.revup.user_service.model.dto;

import com.revup.user_service.model.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest {
    private String firstName;
    private String lastName;
    private String phone;
    private UserRole role;
    private String address;
    private String city;
    private String state;
    private String zipCode;
    private String country;
    private String profileImageUrl;
}
