package com.revup.service_management_service.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateServiceCatalogRequest {

    @NotBlank(message = "Service name is required")
    @Size(max = 200, message = "Service name must not exceed 200 characters")
    private String name;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    @Size(max = 100, message = "Category must not exceed 100 characters")
    private String category;

    @Min(value = 1, message = "Estimated duration must be at least 1 minute")
    private Integer estimatedDuration;

    @DecimalMin(value = "0.0", message = "Base price must be non-negative")
    private BigDecimal basePrice;

    private Boolean isActive;
}
