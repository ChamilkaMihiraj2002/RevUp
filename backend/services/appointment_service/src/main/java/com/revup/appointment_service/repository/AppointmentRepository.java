package com.revup.appointment_service.repository;

import com.revup.appointment_service.model.Appointment;
import com.revup.appointment_service.model.AppointmentStatus;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Repository
public interface AppointmentRepository extends R2dbcRepository<Appointment, Long> {

    Flux<Appointment> findByCustomerId(Long customerId);

    Flux<Appointment> findByVehicleId(Long vehicleId);

    Flux<Appointment> findByEmployeeId(Long employeeId);

    Flux<Appointment> findByStatus(AppointmentStatus status);

    @Query("SELECT * FROM appointment_schema.appointments " +
           "WHERE customer_id = :customerId AND status = :status " +
           "ORDER BY scheduled_date DESC")
    Flux<Appointment> findByCustomerIdAndStatus(Long customerId, AppointmentStatus status);

    @Query("SELECT * FROM appointment_schema.appointments " +
           "WHERE employee_id = :employeeId AND scheduled_date >= :startDate AND scheduled_date < :endDate " +
           "ORDER BY scheduled_date")
    Flux<Appointment> findByEmployeeIdAndDateRange(Long employeeId, LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT * FROM appointment_schema.appointments " +
           "WHERE scheduled_date >= :startDate AND scheduled_date < :endDate " +
           "AND status IN ('PENDING', 'CONFIRMED') " +
           "ORDER BY scheduled_date")
    Flux<Appointment> findUpcomingAppointments(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT COUNT(*) FROM appointment_schema.appointments " +
           "WHERE employee_id = :employeeId " +
           "AND scheduled_date >= :startTime " +
           "AND scheduled_date < :endTime " +
           "AND status NOT IN ('CANCELLED', 'COMPLETED')")
    Mono<Long> countConflictingAppointments(Long employeeId, LocalDateTime startTime, LocalDateTime endTime);

    @Query("SELECT * FROM appointment_schema.appointments " +
           "WHERE scheduled_date >= :today " +
           "AND status = 'PENDING' " +
           "ORDER BY scheduled_date " +
           "LIMIT :limit")
    Flux<Appointment> findPendingAppointmentsFromToday(LocalDateTime today, int limit);
}
