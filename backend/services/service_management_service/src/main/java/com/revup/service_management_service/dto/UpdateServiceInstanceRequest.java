package com.revup.service_management_service.dto;

import com.revup.service_management_service.model.ServiceStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateServiceInstanceRequest {

    private Long employeeId;

    private ServiceStatus status;

    @Min(value = 0, message = "Progress must be at least 0")
    @Max(value = 100, message = "Progress must not exceed 100")
    private Integer progressPercentage;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    @DecimalMin(value = "0.0", message = "Actual price must be non-negative")
    private BigDecimal actualPrice;

    @Min(value = 1, message = "Actual duration must be at least 1 minute")
    private Integer actualDuration;

    private String notes;
}
