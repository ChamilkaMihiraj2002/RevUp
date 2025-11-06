package com.revup.notification_service.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * REST client for communicating with the User Service
 * Used to fetch user details for notifications
 */
@Component
public class UserServiceClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceClient.class);

    private final RestTemplate restTemplate;

    @Value("${services.user-service.url:http://localhost:8081}")
    private String userServiceUrl;

    public UserServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Get user details by user ID
     *
     * @param userId The user ID
     * @return UserDTO with user details
     */
    public UserDTO getUserById(Long userId) {
        try {
            String url = userServiceUrl + "/api/users/" + userId;
            LOGGER.debug("Fetching user details from: {}", url);
            return restTemplate.getForObject(url, UserDTO.class);
        } catch (Exception e) {
            LOGGER.error("Error fetching user details for userId: {}", userId, e);
            return null;
        }
    }

    /**
     * Get user email by user ID
     *
     * @param userId The user ID
     * @return User email address
     */
    public String getUserEmail(Long userId) {
        UserDTO user = getUserById(userId);
        return user != null ? user.getEmail() : null;
    }

    /**
     * Simple DTO for user information
     */
    public static class UserDTO {
        private Long userId;
        private String email;
        private String name;
        private String phone;
        private String role;

        // Getters and Setters
        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }
    }
}

