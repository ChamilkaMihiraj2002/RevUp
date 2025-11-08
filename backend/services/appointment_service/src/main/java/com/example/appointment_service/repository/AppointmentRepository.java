package com.example.appointment_service.repository;

import com.example.appointment_service.entity.Appointment;
import com.example.appointment_service.enums.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    
    List<Appointment> findByCustomerId(Long customerId);
    
    List<Appointment> findByVehicleId(Long vehicleId);
    
    List<Appointment> findByTechnicianId(Long technicianId);
    
    List<Appointment> findByStatus(AppointmentStatus status);
    
    // Find unassigned appointments (technicianId is null and status is SCHEDULED)
    List<Appointment> findByTechnicianIdIsNullAndStatus(AppointmentStatus status);
    
    @Query("SELECT a FROM Appointment a WHERE a.scheduledStart >= :start AND a.scheduledEnd <= :end")
    List<Appointment> findByScheduledBetween(@Param("start") OffsetDateTime start, @Param("end") OffsetDateTime end);
    
    @Query("SELECT a FROM Appointment a WHERE a.technicianId = :technicianId AND a.status NOT IN ('COMPLETED', 'CANCELLED') AND ((a.scheduledStart <= :end AND a.scheduledEnd >= :start))")
    List<Appointment> findOverlappingAppointments(@Param("technicianId") Long technicianId, @Param("start") OffsetDateTime start, @Param("end") OffsetDateTime end);
}
