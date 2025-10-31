package com.revup.employee_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "employees", schema = "employee_schema")
public class Employee {

    @Id
    private Long id;

    @Column("user_id")
    private Long userId;

    @Column("employee_code")
    private String employeeCode;

    @Column("specialization")
    private String specialization;

    @Column("hourly_rate")
    private BigDecimal hourlyRate;

    @Column("availability_status")
    private AvailabilityStatus availabilityStatus;

    @Column("total_services_completed")
    private Integer totalServicesCompleted;

    @Column("average_rating")
    private BigDecimal averageRating;

    @Column("hire_date")
    private LocalDate hireDate;

    @CreatedDate
    @Column("created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column("updated_at")
    private LocalDateTime updatedAt;
}
