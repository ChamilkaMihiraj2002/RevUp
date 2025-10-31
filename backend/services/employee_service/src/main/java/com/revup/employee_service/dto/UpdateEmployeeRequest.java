package com.revup.employee_service.dto;

import com.revup.employee_service.model.AvailabilityStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEmployeeRequest {

    @Size(max = 100, message = "Specialization must not exceed 100 characters")
    private String specialization;

    @DecimalMin(value = "0.0", inclusive = false, message = "Hourly rate must be greater than 0")
    private BigDecimal hourlyRate;

    private AvailabilityStatus availabilityStatus;

    private LocalDate hireDate;
}
