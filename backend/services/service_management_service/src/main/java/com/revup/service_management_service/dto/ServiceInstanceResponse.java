package com.revup.service_management_service.dto;

import com.revup.service_management_service.model.ServiceStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceInstanceResponse {

    private Long id;
    private Long appointmentId;
    private Long serviceCatalogId;
    private Long employeeId;
    private ServiceStatus status;
    private Integer progressPercentage;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private BigDecimal actualPrice;
    private Integer actualDuration;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
