package com.example.project_service.dto;

import com.example.project_service.Enum.ProjectStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Project data transfer object")
public class ProjectDto {
    
    @Schema(description = "Unique identifier of the project", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long projectId;
    
    @Schema(description = "ID of the user who owns this project", example = "123", required = true)
    private Long userId;
    
    @Schema(description = "Detailed description of the project", example = "Oil change and tire rotation")
    private String description;
    
    @Schema(description = "Current status of the project", 
            example = "IN_PROGRESS",
            allowableValues = {"PENDING", "IN_PROGRESS", "COMPLETED", "CANCELLED", "ON_HOLD"})
    private ProjectStatus status;
    
    @Schema(description = "Estimated time to complete the project in hours", example = "5")
    private Integer estimateTime;
    
    @Schema(description = "Start time of the project", example = "2025-11-06T10:00:00")
    private LocalDateTime startTime;
    
    @Schema(description = "End time of the project", example = "2025-11-06T15:00:00")
    private LocalDateTime endTime;
    
    @Schema(description = "Timestamp when the project was created", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createdAt;
    
    @Schema(description = "Timestamp when the project was last updated", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime updatedAt;
}
