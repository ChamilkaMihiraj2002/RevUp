package com.example.appointment_service.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "appointment_services")
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

    @Column(name = "service_type_id", nullable = false)
    private Long serviceTypeId;
}
