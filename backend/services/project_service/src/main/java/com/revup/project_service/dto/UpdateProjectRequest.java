package com.revup.project_service.dto;

import com.revup.project_service.enums.ProjectStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProjectRequest {
    
    private String title;
    private String description;
    private String projectType;
    private ProjectStatus status;
    private BigDecimal estimatedCost;
    private BigDecimal actualCost;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
