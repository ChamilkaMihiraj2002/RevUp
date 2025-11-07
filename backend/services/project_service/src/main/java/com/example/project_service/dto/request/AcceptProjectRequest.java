package com.example.project_service.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO for technician accepting a project
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request object for technician to accept a project")
public class AcceptProjectRequest {

    @NotNull(message = "Technician ID is required")
    @Positive(message = "Technician ID must be positive")
    @Schema(description = "ID of the technician accepting the project", example = "789", required = true)
    private Long technicianId;

    @NotNull(message = "Estimated amount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Estimated amount must be greater than 0")
    @Schema(description = "Estimated cost for the project in USD", example = "250.00", required = true)
    private BigDecimal estimatedAmount;

    @NotNull(message = "Estimate time is required")
    @Positive(message = "Estimate time must be positive")
    @Schema(description = "Estimated time to complete the project in hours", example = "8", required = true)
    private Integer estimateTime;
}
