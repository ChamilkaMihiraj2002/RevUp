package com.revup.customer_service.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCustomerRequest {
    
    @NotNull(message = "User ID is required")
    private Long userId;
    
    private String companyName;
    private String preferences;
    private String notes;
}
