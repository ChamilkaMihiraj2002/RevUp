package com.example.appointment_service.service.impl;

import com.example.appointment_service.dto.*;
import com.example.appointment_service.entity.Appointment;
import com.example.appointment_service.mapper.AppointmentMapper;
import com.example.appointment_service.repository.AppointmentRepository;
import com.example.appointment_service.service.AppointmentServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.OffsetDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentServiceInterface {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentMapper appointmentMapper;

    @Override
    public Mono<AppointmentResponse> createAppointment(CreateAppointmentRequest request) {
        return Mono.fromCallable(() -> {
            Appointment appointment = appointmentMapper.toEntity(request);
            Appointment saved = appointmentRepository.save(appointment);
            return appointmentMapper.toResponse(saved);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<AppointmentResponse> getAppointmentById(Long id) {
        return Mono.fromCallable(() -> appointmentRepository.findById(id)
                .map(appointmentMapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Appointment not found"))
        ).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Flux<AppointmentResponse> getAllAppointments() {
        return Flux.defer(() -> Flux.fromIterable(appointmentRepository.findAll()))
                .map(appointmentMapper::toResponse)
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<AppointmentResponse> updateAppointment(Long id, UpdateAppointmentRequest request) {
        return Mono.fromCallable(() -> {
            Appointment existing = appointmentRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Appointment not found"));

            if (request.getTechnicianId() != null)
                existing.setTechnicianId(request.getTechnicianId());
            if (request.getScheduledStart() != null)
                existing.setScheduledStart(request.getScheduledStart().atOffset(OffsetDateTime.now().getOffset()));
            if (request.getScheduledEnd() != null)
                existing.setScheduledEnd(request.getScheduledEnd().atOffset(OffsetDateTime.now().getOffset()));
            if (request.getStatus() != null)
                existing.setStatus(request.getStatus());

            existing.setUpdatedAt(OffsetDateTime.now());

            Appointment updated = appointmentRepository.save(existing);
            return appointmentMapper.toResponse(updated);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<Void> deleteAppointment(Long id) {
        return Mono.fromRunnable(() -> appointmentRepository.deleteById(id))
                .subscribeOn(Schedulers.boundedElastic())
                .then();
    }
}
