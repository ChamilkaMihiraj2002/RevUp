package com.revup.timelog_service.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTimeLogRequest {
    
    @NotNull(message = "Employee ID is required")
    private Long employeeId;
    
    private Long serviceInstanceId;
    
    private Long projectId;
    
    @NotNull(message = "Log date is required")
    private LocalDate logDate;
    
    @NotNull(message = "Hours worked is required")
    @DecimalMin(value = "0.01", message = "Hours must be greater than 0")
    @DecimalMax(value = "24.00", message = "Hours cannot exceed 24")
    private BigDecimal hoursWorked;
    
    private String description;
    
    @NotNull(message = "Billable flag is required")
    private Boolean billable;
}
