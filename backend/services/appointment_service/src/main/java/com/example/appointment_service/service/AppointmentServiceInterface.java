package com.example.appointment_service.service;

import com.example.appointment_service.dto.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AppointmentServiceInterface {
    Mono<AppointmentResponse> createAppointment(CreateAppointmentRequest request);
    Mono<AppointmentResponse> getAppointmentById(Long id);
    Flux<AppointmentResponse> getAllAppointments();
    Mono<AppointmentResponse> updateAppointment(Long id, UpdateAppointmentRequest request);
    Mono<Void> deleteAppointment(Long id);
}
