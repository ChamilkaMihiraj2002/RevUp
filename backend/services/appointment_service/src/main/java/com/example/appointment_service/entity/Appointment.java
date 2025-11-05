package com.example.appointment_service.entity;

import com.example.appointment_service.enums.AppointmentStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "appointments", indexes = {
    @Index(name = "idx_customer_id", columnList = "customerId"),
    @Index(name = "idx_vehicle_id", columnList = "vehicleId"),
    @Index(name = "idx_technician_id", columnList = "technicianId"),
    @Index(name = "idx_status", columnList = "status"),
    @Index(name = "idx_scheduled_start", columnList = "scheduledStart")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long appointmentId;

    @Column(nullable = false)
    private Long customerId;
    
    @Column(nullable = false)
    private Long vehicleId;
    
    private Long technicianId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentStatus status;

    @Column(nullable = false)
    private OffsetDateTime scheduledStart;
    
    @Column(nullable = false)
    private OffsetDateTime scheduledEnd;

    @Column(nullable = false, updatable = false)
    private OffsetDateTime createdAt;
    
    @Column(nullable = false)
    private OffsetDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        OffsetDateTime now = OffsetDateTime.now();
        createdAt = now;
        updatedAt = now;
        if (status == null) {
            status = AppointmentStatus.SCHEDULED;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = OffsetDateTime.now();
    }
}

