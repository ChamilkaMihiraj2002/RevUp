package com.revup.timelog_service.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "time_logs", schema = "timelog_schema")
public class TimeLog {
    
    @Id
    private Long id;
    
    @Column("employee_id")
    private Long employeeId;
    
    @Column("service_instance_id")
    private Long serviceInstanceId;
    
    @Column("project_id")
    private Long projectId;
    
    @Column("log_date")
    private LocalDate logDate;
    
    @Column("hours_worked")
    private BigDecimal hoursWorked;
    
    @Column("description")
    private String description;
    
    @Column("billable")
    private Boolean billable;
    
    @Column("created_at")
    private LocalDateTime createdAt;
    
    @Column("updated_at")
    private LocalDateTime updatedAt;
}
