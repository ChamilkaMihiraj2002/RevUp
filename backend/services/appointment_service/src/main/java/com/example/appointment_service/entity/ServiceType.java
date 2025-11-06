package com.example.appointment_service.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "service_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long serviceTypeId;

    @Column(nullable = false, unique = true)
    private String code;  // Example: OIL_CHANGE, ENGINE_CHECK

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private Integer baseDurationMinutes;

    @Column(nullable = false)
    private Double basePrice;
}
