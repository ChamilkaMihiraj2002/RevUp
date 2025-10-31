package com.revup.timelog_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimeLogResponse {
    private Long id;
    private Long employeeId;
    private Long serviceInstanceId;
    private Long projectId;
    private LocalDate logDate;
    private BigDecimal hoursWorked;
    private String description;
    private Boolean billable;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
