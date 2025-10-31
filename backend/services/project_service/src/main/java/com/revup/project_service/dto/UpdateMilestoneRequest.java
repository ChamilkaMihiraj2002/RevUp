package com.revup.project_service.dto;

import com.revup.project_service.enums.MilestoneStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMilestoneRequest {
    private String title;
    private String description;
    private MilestoneStatus status;
    private LocalDateTime dueDate;
    private LocalDateTime completedDate;
}
