package com.revup.appointment_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "appointments", schema = "appointment_schema")
public class Appointment {

    @Id
    private Long id;

    @Column("customer_id")
    private Long customerId;

    @Column("vehicle_id")
    private Long vehicleId;

    @Column("employee_id")
    private Long employeeId;

    @Column("service_type")
    private String serviceType;

    @Column("scheduled_date")
    private LocalDateTime scheduledDate;

    @Column("status")
    private AppointmentStatus status;

    @Column("estimated_duration")
    private Integer estimatedDuration;

    @Column("notes")
    private String notes;

    @CreatedDate
    @Column("created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column("updated_at")
    private LocalDateTime updatedAt;
}
