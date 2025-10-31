package com.revup.service_management_service.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProgressUpdateRequest {

    @NotNull(message = "Service instance ID is required")
    private Long serviceInstanceId;

    @NotNull(message = "Progress percentage is required")
    @Min(value = 0, message = "Progress must be at least 0")
    @Max(value = 100, message = "Progress must not exceed 100")
    private Integer progressPercentage;

    private String notes;
}
