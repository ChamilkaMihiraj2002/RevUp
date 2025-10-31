package com.revup.service_management_service.dto;

import com.revup.service_management_service.model.ServiceStatus;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateServiceInstanceRequest {

    @NotNull(message = "Appointment ID is required")
    private Long appointmentId;

    @NotNull(message = "Service catalog ID is required")
    private Long serviceCatalogId;

    private Long employeeId;

    private ServiceStatus status;

    @Min(value = 0, message = "Progress must be at least 0")
    @Max(value = 100, message = "Progress must not exceed 100")
    private Integer progressPercentage;

    private LocalDateTime startDate;

    @DecimalMin(value = "0.0", message = "Actual price must be non-negative")
    private BigDecimal actualPrice;

    private String notes;
}
