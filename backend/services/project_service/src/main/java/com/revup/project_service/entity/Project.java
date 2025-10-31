package com.revup.project_service.entity;

import com.revup.project_service.enums.ProjectStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "projects", schema = "project_schema")
public class Project {
    
    @Id
    private Long id;
    
    @Column("customer_id")
    private Long customerId;
    
    @Column("vehicle_id")
    private Long vehicleId;
    
    @Column("project_code")
    private String projectCode;
    
    @Column("title")
    private String title;
    
    @Column("description")
    private String description;
    
    @Column("project_type")
    private String projectType;
    
    @Column("status")
    private ProjectStatus status;
    
    @Column("estimated_cost")
    private BigDecimal estimatedCost;
    
    @Column("actual_cost")
    private BigDecimal actualCost;
    
    @Column("start_date")
    private LocalDateTime startDate;
    
    @Column("end_date")
    private LocalDateTime endDate;
    
    @Column("created_at")
    private LocalDateTime createdAt;
    
    @Column("updated_at")
    private LocalDateTime updatedAt;
}
