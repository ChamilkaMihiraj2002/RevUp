package com.example.appointment_service.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "appointment_services", indexes = {
    @Index(name = "idx_appointment_id", columnList = "appointment_id"),
    @Index(name = "idx_service_type_id", columnList = "service_type_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long appointmentServiceId;

    private Integer actualMinutes;
    private String status;
    private Integer quantity;
    private Integer estimatedMinutes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id", nullable = false)
    private Appointment appointment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_type_id", nullable = false)
    private ServiceType serviceType;
}
