package com.example.project_service.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for updating an existing project
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request object for updating an existing project")
public class UpdateProjectRequest {

    @Schema(description = "Updated description of the project", 
            example = "Oil change, tire rotation, and brake inspection")
    private String description;

    @Schema(description = "Updated status of the project", 
            example = "IN_PROGRESS",
            allowableValues = {"PENDING", "IN_PROGRESS", "COMPLETED", "CANCELLED", "ON_HOLD"})
    private String status; // PENDING, IN_PROGRESS, COMPLETED, CANCELLED, ON_HOLD

    @Positive(message = "Estimate time must be positive")
    @Schema(description = "Updated estimated time in hours", example = "6")
    private Integer estimateTime; // in hours

    @Schema(description = "Updated start time of the project", example = "2025-11-10T10:00:00")
    private LocalDateTime startTime;

    @Schema(description = "Updated end time of the project", example = "2025-11-10T16:00:00")
    private LocalDateTime endTime;
}
