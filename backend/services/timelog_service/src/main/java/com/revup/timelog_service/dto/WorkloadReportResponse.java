package com.revup.timelog_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkloadReportResponse {
    private Long employeeId;
    private Integer totalDays;
    private BigDecimal totalHours;
    private BigDecimal billableHours;
    private BigDecimal nonBillableHours;
    private BigDecimal averageHoursPerDay;
}
