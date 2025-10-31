package com.revup.employee_service.dto;

import com.revup.employee_service.model.AvailabilityStatus;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateEmployeeRequest {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotBlank(message = "Specialization is required")
    @Size(max = 100, message = "Specialization must not exceed 100 characters")
    private String specialization;

    @DecimalMin(value = "0.0", inclusive = false, message = "Hourly rate must be greater than 0")
    private BigDecimal hourlyRate;

    private AvailabilityStatus availabilityStatus;

    private LocalDate hireDate;
}
