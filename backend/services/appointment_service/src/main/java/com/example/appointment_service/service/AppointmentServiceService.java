package com.example.appointment_service.service;

import com.example.appointment_service.dto.CreateAppointmentServiceRequest;
import com.example.appointment_service.dto.AppointmentServiceResponse;
import reactor.core.publisher.Mono;

import java.util.List;

public interface AppointmentServiceService {
    Mono<AppointmentServiceResponse> create(CreateAppointmentServiceRequest request);
    Mono<AppointmentServiceResponse> getById(Long id);
    Mono<List<AppointmentServiceResponse>> getAll();
    Mono<Void> deleteById(Long id);
}
