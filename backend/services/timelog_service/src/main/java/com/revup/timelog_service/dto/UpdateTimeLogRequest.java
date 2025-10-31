package com.revup.timelog_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTimeLogRequest {
    private LocalDate logDate;
    private BigDecimal hoursWorked;
    private String description;
    private Boolean billable;
}
