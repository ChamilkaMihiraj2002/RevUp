package com.example.UserService.dto;

import com.example.UserService.Enum.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long userId;
    private String name;
    private String email;
    private String phone;
    private String address;
    private Role role;
    private String firebaseUID;
    private List<Long> vehicleIds;
}
