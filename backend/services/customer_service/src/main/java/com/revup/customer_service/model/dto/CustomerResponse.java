package com.revup.customer_service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponse {
    private Long id;
    private Long userId;
    private String customerCode;
    private String companyName;
    private String preferences;
    private Integer loyaltyPoints;
    private BigDecimal totalSpent;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
