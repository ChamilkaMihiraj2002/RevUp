package com.revup.employee_service.dto;

import com.revup.employee_service.model.AvailabilityStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponse {

    private Long id;
    private Long userId;
    private String employeeCode;
    private String specialization;
    private BigDecimal hourlyRate;
    private AvailabilityStatus availabilityStatus;
    private Integer totalServicesCompleted;
    private BigDecimal averageRating;
    private LocalDate hireDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
