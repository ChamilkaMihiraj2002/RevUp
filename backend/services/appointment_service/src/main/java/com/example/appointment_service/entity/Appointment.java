package com.example.appointment_service.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "appointments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long appointmentId;

    private Long customerId;
    private Long vehicleId;
    private Long technicianId;

    @Column(nullable = false)
    private String status; // e.g. SCHEDULED, COMPLETED, CANCELLED

    private OffsetDateTime scheduledStart;
    private OffsetDateTime scheduledEnd;

    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}

