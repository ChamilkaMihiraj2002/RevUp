package com.revup.appointment_service.service;

import com.revup.appointment_service.dto.AppointmentResponse;
import com.revup.appointment_service.dto.CreateAppointmentRequest;
import com.revup.appointment_service.dto.UpdateAppointmentRequest;
import com.revup.appointment_service.exception.DuplicateResourceException;
import com.revup.appointment_service.exception.ResourceNotFoundException;
import com.revup.appointment_service.mapper.AppointmentMapper;
import com.revup.appointment_service.model.Appointment;
import com.revup.appointment_service.model.AppointmentStatus;
import com.revup.appointment_service.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentMapper appointmentMapper;

    @Transactional
    public Mono<AppointmentResponse> createAppointment(CreateAppointmentRequest request) {
        log.info("Creating appointment for customer ID: {} and vehicle ID: {}", 
                request.getCustomerId(), request.getVehicleId());

        // Check for scheduling conflicts if employee is assigned
        if (request.getEmployeeId() != null) {
            LocalDateTime scheduledStart = request.getScheduledDate();
            int duration = request.getEstimatedDuration() != null ? request.getEstimatedDuration() : 60;
            LocalDateTime scheduledEnd = scheduledStart.plusMinutes(duration);

            return appointmentRepository.countConflictingAppointments(
                    request.getEmployeeId(), scheduledStart, scheduledEnd)
                    .flatMap(count -> {
                        if (count > 0) {
                            return Mono.error(new DuplicateResourceException(
                                    "Employee is not available at the requested time"));
                        }
                        return createAndSaveAppointment(request);
                    });
        }

        return createAndSaveAppointment(request);
    }

    private Mono<AppointmentResponse> createAndSaveAppointment(CreateAppointmentRequest request) {
        Appointment appointment = appointmentMapper.toEntity(request);
        appointment.setStatus(AppointmentStatus.PENDING);
        if (appointment.getEstimatedDuration() == null) {
            appointment.setEstimatedDuration(60); // Default 1 hour
        }
        appointment.setCreatedAt(LocalDateTime.now());
        appointment.setUpdatedAt(LocalDateTime.now());

        return appointmentRepository.save(appointment)
                .map(appointmentMapper::toResponse);
    }

    public Mono<AppointmentResponse> getAppointmentById(Long id) {
        log.info("Fetching appointment with ID: {}", id);
        return appointmentRepository.findById(id)
                .map(appointmentMapper::toResponse)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Appointment not found with ID: " + id)));
    }

    public Flux<AppointmentResponse> getAllAppointments() {
        log.info("Fetching all appointments");
        return appointmentRepository.findAll()
                .map(appointmentMapper::toResponse);
    }

    public Flux<AppointmentResponse> getAppointmentsByCustomerId(Long customerId) {
        log.info("Fetching appointments for customer ID: {}", customerId);
        return appointmentRepository.findByCustomerId(customerId)
                .map(appointmentMapper::toResponse);
    }

    public Flux<AppointmentResponse> getAppointmentsByVehicleId(Long vehicleId) {
        log.info("Fetching appointments for vehicle ID: {}", vehicleId);
        return appointmentRepository.findByVehicleId(vehicleId)
                .map(appointmentMapper::toResponse);
    }

    public Flux<AppointmentResponse> getAppointmentsByEmployeeId(Long employeeId) {
        log.info("Fetching appointments for employee ID: {}", employeeId);
        return appointmentRepository.findByEmployeeId(employeeId)
                .map(appointmentMapper::toResponse);
    }

    public Flux<AppointmentResponse> getAppointmentsByStatus(AppointmentStatus status) {
        log.info("Fetching appointments with status: {}", status);
        return appointmentRepository.findByStatus(status)
                .map(appointmentMapper::toResponse);
    }

    public Flux<AppointmentResponse> getUpcomingAppointments(int daysAhead) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime futureDate = now.plusDays(daysAhead);
        log.info("Fetching upcoming appointments from {} to {}", now, futureDate);
        return appointmentRepository.findUpcomingAppointments(now, futureDate)
                .map(appointmentMapper::toResponse);
    }

    public Flux<AppointmentResponse> getEmployeeSchedule(Long employeeId, LocalDateTime startDate, LocalDateTime endDate) {
        log.info("Fetching schedule for employee ID: {} from {} to {}", employeeId, startDate, endDate);
        return appointmentRepository.findByEmployeeIdAndDateRange(employeeId, startDate, endDate)
                .map(appointmentMapper::toResponse);
    }

    @Transactional
    public Mono<AppointmentResponse> updateAppointment(Long id, UpdateAppointmentRequest request) {
        log.info("Updating appointment with ID: {}", id);
        return appointmentRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Appointment not found with ID: " + id)))
                .flatMap(appointment -> {
                    appointmentMapper.updateAppointmentFromRequest(request, appointment);
                    appointment.setUpdatedAt(LocalDateTime.now());
                    return appointmentRepository.save(appointment);
                })
                .map(appointmentMapper::toResponse);
    }

    @Transactional
    public Mono<AppointmentResponse> updateAppointmentStatus(Long id, AppointmentStatus status) {
        log.info("Updating status for appointment ID: {} to {}", id, status);
        return appointmentRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Appointment not found with ID: " + id)))
                .flatMap(appointment -> {
                    appointment.setStatus(status);
                    appointment.setUpdatedAt(LocalDateTime.now());
                    return appointmentRepository.save(appointment);
                })
                .map(appointmentMapper::toResponse);
    }

    @Transactional
    public Mono<AppointmentResponse> assignEmployee(Long appointmentId, Long employeeId) {
        log.info("Assigning employee ID: {} to appointment ID: {}", employeeId, appointmentId);
        return appointmentRepository.findById(appointmentId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Appointment not found with ID: " + appointmentId)))
                .flatMap(appointment -> {
                    // Check for conflicts before assigning
                    LocalDateTime scheduledStart = appointment.getScheduledDate();
                    LocalDateTime scheduledEnd = scheduledStart.plusMinutes(appointment.getEstimatedDuration());

                    return appointmentRepository.countConflictingAppointments(employeeId, scheduledStart, scheduledEnd)
                            .flatMap(count -> {
                                if (count > 0) {
                                    return Mono.error(new DuplicateResourceException(
                                            "Employee is not available at the appointment time"));
                                }
                                appointment.setEmployeeId(employeeId);
                                appointment.setUpdatedAt(LocalDateTime.now());
                                return appointmentRepository.save(appointment);
                            });
                })
                .map(appointmentMapper::toResponse);
    }

    @Transactional
    public Mono<Void> deleteAppointment(Long id) {
        log.info("Deleting appointment with ID: {}", id);
        return appointmentRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Appointment not found with ID: " + id)))
                .flatMap(appointmentRepository::delete);
    }

    @Transactional
    public Mono<AppointmentResponse> cancelAppointment(Long id) {
        log.info("Cancelling appointment with ID: {}", id);
        return updateAppointmentStatus(id, AppointmentStatus.CANCELLED);
    }

    @Transactional
    public Mono<AppointmentResponse> confirmAppointment(Long id) {
        log.info("Confirming appointment with ID: {}", id);
        return updateAppointmentStatus(id, AppointmentStatus.CONFIRMED);
    }

    public Flux<AppointmentResponse> getPendingAppointments(int limit) {
        log.info("Fetching pending appointments");
        return appointmentRepository.findPendingAppointmentsFromToday(LocalDateTime.now(), limit)
                .map(appointmentMapper::toResponse);
    }
}
