package com.example.project_service.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for creating a new project
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request object for creating a new project")
public class CreateProjectRequest {

    @NotNull(message = "User ID is required")
    @Positive(message = "User ID must be positive")
    @Schema(description = "ID of the user who owns this project", example = "123", required = true)
    private Long userId;

    @NotBlank(message = "Description is required")
    @Schema(description = "Detailed description of the project", 
            example = "Full vehicle inspection and brake system repair", 
            required = true)
    private String description;

    @Schema(description = "Initial status of the project (defaults to PENDING if not provided)", 
            example = "PENDING",
            allowableValues = {"PENDING", "IN_PROGRESS", "COMPLETED", "CANCELLED", "ON_HOLD"})
    private String status; // PENDING, IN_PROGRESS, COMPLETED, CANCELLED, ON_HOLD

    @Positive(message = "Estimate time must be positive")
    @Schema(description = "Estimated time to complete the project in hours", example = "8")
    private Integer estimateTime; // in hours

    @Schema(description = "Scheduled start time of the project", example = "2025-11-10T09:00:00")
    private LocalDateTime startTime;

    @Schema(description = "Expected end time of the project", example = "2025-11-10T17:00:00")
    private LocalDateTime endTime;
}
