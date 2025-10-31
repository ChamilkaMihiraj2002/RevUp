package com.revup.service_management_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "service_instances", schema = "service_schema")
public class ServiceInstance {

    @Id
    private Long id;

    @Column("appointment_id")
    private Long appointmentId;

    @Column("service_catalog_id")
    private Long serviceCatalogId;

    @Column("employee_id")
    private Long employeeId;

    @Column("status")
    private ServiceStatus status;

    @Column("progress_percentage")
    private Integer progressPercentage;

    @Column("start_date")
    private LocalDateTime startDate;

    @Column("end_date")
    private LocalDateTime endDate;

    @Column("actual_price")
    private BigDecimal actualPrice;

    @Column("actual_duration")
    private Integer actualDuration;

    @Column("notes")
    private String notes;

    @CreatedDate
    @Column("created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column("updated_at")
    private LocalDateTime updatedAt;
}
