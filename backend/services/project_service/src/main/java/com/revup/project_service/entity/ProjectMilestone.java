package com.revup.project_service.entity;

import com.revup.project_service.enums.MilestoneStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "project_milestones", schema = "project_schema")
public class ProjectMilestone {
    
    @Id
    private Long id;
    
    @Column("project_id")
    private Long projectId;
    
    @Column("title")
    private String title;
    
    @Column("description")
    private String description;
    
    @Column("status")
    private MilestoneStatus status;
    
    @Column("due_date")
    private LocalDateTime dueDate;
    
    @Column("completed_date")
    private LocalDateTime completedDate;
    
    @Column("created_at")
    private LocalDateTime createdAt;
    
    @Column("updated_at")
    private LocalDateTime updatedAt;
}
