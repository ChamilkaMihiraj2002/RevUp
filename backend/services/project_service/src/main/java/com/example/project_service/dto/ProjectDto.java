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
    
    @Schema(description = "ID of the vehicle associated with this project", example = "456")
    private Long vehicleId;
    
    @Schema(description = "ID of the technician assigned to this project", example = "789")
    private Long technicianId;
    
    @Schema(description = "Detailed description of the project", example = "Oil change and tire rotation")
    private String description;
    
    @Schema(description = "Current status of the project", 
            example = "IN_PROGRESS",
            allowableValues = {"PENDING", "IN_PROGRESS", "COMPLETED", "CANCELLED", "ON_HOLD"})
    private ProjectStatus status;
    
    @Schema(description = "Estimated time to complete the project in hours", example = "5")
    private Integer estimateTime;
    
    @Schema(description = "Estimated amount for the project in USD", example = "250.00")
    private java.math.BigDecimal estimatedAmount;
    
    @Schema(description = "Timestamp when the project was created", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createdAt;
    
    @Schema(description = "Timestamp when the project was last updated", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime updatedAt;
}
