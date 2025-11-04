package com.example.appointment_service.service.impl;

import com.example.appointment_service.dto.CreateAppointmentServiceRequest;
import com.example.appointment_service.dto.AppointmentServiceResponse;
import com.example.appointment_service.entity.Appointment;
import com.example.appointment_service.entity.AppointmentService;
import com.example.appointment_service.exception.ResourceNotFoundException;
import com.example.appointment_service.mapper.AppointmentServiceMapper;
import com.example.appointment_service.repository.AppointmentRepository;
import com.example.appointment_service.repository.AppointmentServiceRepository;
import com.example.appointment_service.service.AppointmentServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentServiceServiceImpl implements AppointmentServiceService {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentServiceRepository appointmentServiceRepository;

    @Override
    public Mono<AppointmentServiceResponse> create(CreateAppointmentServiceRequest request) {
        return Mono.fromCallable(() -> {
            Appointment appointment = appointmentRepository.findById(request.getAppointmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + request.getAppointmentId()));

            AppointmentService entity = AppointmentServiceMapper.toEntity(request, appointment);
            AppointmentService saved = appointmentServiceRepository.save(entity);
            return AppointmentServiceMapper.toResponse(saved);
        });
    }

    @Override
    public Mono<AppointmentServiceResponse> getById(Long id) {
        return Mono.fromCallable(() -> appointmentServiceRepository.findById(id)
                .map(AppointmentServiceMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("AppointmentService not found with id: " + id)));
    }

    @Override
    public Mono<List<AppointmentServiceResponse>> getAll() {
        return Mono.fromCallable(() -> appointmentServiceRepository.findAll().stream()
                .map(AppointmentServiceMapper::toResponse)
                .collect(Collectors.toList()));
    }

    @Override
    public Mono<Void> deleteById(Long id) {
        return Mono.fromRunnable(() -> {
            if (!appointmentServiceRepository.existsById(id)) {
                throw new ResourceNotFoundException("AppointmentService not found with id: " + id);
            }
            appointmentServiceRepository.deleteById(id);
        });
    }
}

