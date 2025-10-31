package com.revup.customer_service.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "customers", schema = "customer_schema")
public class Customer {
    
    @Id
    private Long id;
    
    @Column("user_id")
    private Long userId;
    
    @Column("customer_code")
    private String customerCode;
    
    @Column("company_name")
    private String companyName;
    
    @Column("preferences")
    private String preferences;
    
    @Column("loyalty_points")
    private Integer loyaltyPoints;
    
    @Column("total_spent")
    private BigDecimal totalSpent;
    
    @Column("notes")
    private String notes;
    
    @CreatedDate
    @Column("created_at")
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Column("updated_at")
    private LocalDateTime updatedAt;
}
