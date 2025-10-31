package com.revup.service_management_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceCatalogResponse {

    private Long id;
    private String name;
    private String description;
    private String category;
    private Integer estimatedDuration;
    private BigDecimal basePrice;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
