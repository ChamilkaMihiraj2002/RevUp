package com.example.appointment_service.repository;

import com.example.appointment_service.entity.AppointmentService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentServiceRepository extends JpaRepository<AppointmentService, Long> {
}
