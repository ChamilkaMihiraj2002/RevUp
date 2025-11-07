package com.example.appointment_service.service.impl;

import com.example.appointment_service.dto.*;
import com.example.appointment_service.entity.Appointment;
import com.example.appointment_service.exception.ResourceNotFoundException;
import com.example.appointment_service.mapper.AppointmentMapper;
import com.example.appointment_service.repository.AppointmentRepository;
import com.example.appointment_service.service.AppointmentServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentServiceInterface {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentMapper appointmentMapper;

    @Override
    public Mono<AppointmentResponse> createAppointment(CreateAppointmentRequest request) {
        return Mono.fromCallable(() -> {
            if (request.getScheduledEnd().isBefore(request.getScheduledStart())) {
                throw new IllegalArgumentException("End time must be after start time");
            }
            
            Appointment appointment = appointmentMapper.toEntity(request);
            Appointment saved = appointmentRepository.save(appointment);
            return appointmentMapper.toResponse(saved);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<AppointmentResponse> getAppointmentById(Long id) {
        return Mono.fromCallable(() -> appointmentRepository.findById(id)
                .map(appointmentMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + id))
        ).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Flux<AppointmentResponse> getAllAppointments() {
        return Flux.defer(() -> Flux.fromIterable(appointmentRepository.findAll()))
                .map(appointmentMapper::toResponse)
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Flux<AppointmentResponse> getAppointmentsByCustomerId(Long customerId) {
        return Flux.defer(() -> Flux.fromIterable(appointmentRepository.findByCustomerId(customerId)))
                .map(appointmentMapper::toResponse)
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Flux<AppointmentResponse> getAppointmentsByVehicleId(Long vehicleId) {
        return Flux.defer(() -> Flux.fromIterable(appointmentRepository.findByVehicleId(vehicleId)))
                .map(appointmentMapper::toResponse)
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Flux<AppointmentResponse> getAppointmentsByTechnicianId(Long technicianId) {
        return Flux.defer(() -> Flux.fromIterable(appointmentRepository.findByTechnicianId(technicianId)))
                .map(appointmentMapper::toResponse)
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Flux<AppointmentResponse> getAppointmentsByStatus(String status) {
        return Flux.defer(() -> {
            try {
                com.example.appointment_service.enums.AppointmentStatus appointmentStatus = 
                    com.example.appointment_service.enums.AppointmentStatus.valueOf(status.toUpperCase());
                return Flux.fromIterable(appointmentRepository.findByStatus(appointmentStatus));
            } catch (IllegalArgumentException e) {
                log.error("Invalid status: {}", status);
                return Flux.empty();
            }
        })
        .map(appointmentMapper::toResponse)
        .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Flux<AppointmentResponse> getUnassignedAppointments() {
        return Flux.defer(() -> 
            Flux.fromIterable(appointmentRepository.findByTechnicianIdIsNullAndStatus(
                com.example.appointment_service.enums.AppointmentStatus.SCHEDULED
            ))
        )
        .map(appointmentMapper::toResponse)
        .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<AppointmentResponse> updateAppointment(Long id, UpdateAppointmentRequest request) {
        return Mono.fromCallable(() -> {
            Appointment existing = appointmentRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + id));

            if (request.getTechnicianId() != null) {
                existing.setTechnicianId(request.getTechnicianId());
            }
            
            if (request.getScheduledStart() != null) {
                existing.setScheduledStart(request.getScheduledStart().atOffset(ZoneOffset.UTC));
            }
            
            if (request.getScheduledEnd() != null) {
                existing.setScheduledEnd(request.getScheduledEnd().atOffset(ZoneOffset.UTC));
            }
            
            if (request.getStatus() != null) {
                existing.setStatus(request.getStatus());
            }

            if (existing.getScheduledEnd().isBefore(existing.getScheduledStart())) {
                throw new IllegalArgumentException("End time must be after start time");
            }

            Appointment updated = appointmentRepository.save(existing);
            return appointmentMapper.toResponse(updated);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<Void> deleteAppointment(Long id) {
        return Mono.fromRunnable(() -> {
            if (!appointmentRepository.existsById(id)) {
                throw new ResourceNotFoundException("Appointment not found with id: " + id);
            }
            appointmentRepository.deleteById(id);
        }).subscribeOn(Schedulers.boundedElastic()).then();
    }

    @Override
    public Mono<SlotAvailabilityResponse> checkSlotAvailability(LocalDate date, LocalTime time) {
        return Mono.fromCallable(() -> {
            // Maximum number of concurrent appointments (5 technicians)
            final int MAX_CAPACITY = 5;
            
            // Calculate start and end time for the 2-hour slot
            LocalDateTime slotStart = LocalDateTime.of(date, time);
            LocalDateTime slotEnd = slotStart.plusHours(2);
            
            // Convert to OffsetDateTime for database query
            var startOffset = slotStart.atOffset(ZoneOffset.UTC);
            var endOffset = slotEnd.atOffset(ZoneOffset.UTC);
            
            // Count appointments in this time slot (excluding CANCELLED and COMPLETED)
            long bookedCount = appointmentRepository.findByScheduledBetween(startOffset, endOffset)
                    .stream()
                    .filter(apt -> !apt.getStatus().name().equals("CANCELLED") && 
                                   !apt.getStatus().name().equals("COMPLETED"))
                    .count();
            
            boolean available = bookedCount < MAX_CAPACITY;
            String message = available 
                    ? String.format("%d slots available", MAX_CAPACITY - bookedCount)
                    : "Slot is full";
            
            return SlotAvailabilityResponse.builder()
                    .available(available)
                    .bookedCount((int) bookedCount)
                    .maxCapacity(MAX_CAPACITY)
                    .message(message)
                    .build();
        }).subscribeOn(Schedulers.boundedElastic());
    }
}
