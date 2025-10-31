package com.revup.project_service.dto;

import com.revup.project_service.enums.ProjectStatus;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateProjectRequest {
    
    @NotNull(message = "Customer ID is required")
    private Long customerId;
    
    @NotNull(message = "Vehicle ID is required")
    private Long vehicleId;
    
    @NotBlank(message = "Title is required")
    @Size(max = 200, message = "Title cannot exceed 200 characters")
    private String title;
    
    private String description;
    
    @Size(max = 50, message = "Project type cannot exceed 50 characters")
    private String projectType;
    
    @DecimalMin(value = "0.0", inclusive = false, message = "Estimated cost must be greater than 0")
    private BigDecimal estimatedCost;
    
    private LocalDateTime startDate;
}
