package com.revup.api_gateway.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {
    private Long userId;
    private String firebaseUID;
    private String email;
    private String firstName;
    private String lastName;
    private Role role;
}
