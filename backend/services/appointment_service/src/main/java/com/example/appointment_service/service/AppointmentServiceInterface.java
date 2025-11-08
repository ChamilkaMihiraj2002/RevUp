package com.example.appointment_service.service;

import com.example.appointment_service.dto.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalTime;

public interface AppointmentServiceInterface {
    Mono<AppointmentResponse> createAppointment(CreateAppointmentRequest request);
    Mono<AppointmentResponse> getAppointmentById(Long id);
    Flux<AppointmentResponse> getAllAppointments();
    Flux<AppointmentResponse> getAppointmentsByCustomerId(Long customerId);
    Flux<AppointmentResponse> getAppointmentsByVehicleId(Long vehicleId);
    Flux<AppointmentResponse> getAppointmentsByTechnicianId(Long technicianId);
    Flux<AppointmentResponse> getAppointmentsByStatus(String status);
    Flux<AppointmentResponse> getUnassignedAppointments();
    Mono<AppointmentResponse> updateAppointment(Long id, UpdateAppointmentRequest request);
    Mono<Void> deleteAppointment(Long id);
    Mono<SlotAvailabilityResponse> checkSlotAvailability(LocalDate date, LocalTime time);
}
