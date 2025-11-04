package com.example.appointment_service.controller;

import com.example.appointment_service.dto.*;
import com.example.appointment_service.service.AppointmentServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentServiceInterface appointmentService;

    @PostMapping
    public Mono<AppointmentResponse> create(@RequestBody CreateAppointmentRequest request) {
        return appointmentService.createAppointment(request);
    }

    @GetMapping("/{id}")
    public Mono<AppointmentResponse> getById(@PathVariable Long id) {
        return appointmentService.getAppointmentById(id);
    }

    @GetMapping
    public Flux<AppointmentResponse> getAll() {
        return appointmentService.getAllAppointments();
    }

    @PutMapping("/{id}")
    public Mono<AppointmentResponse> update(@PathVariable Long id, @RequestBody UpdateAppointmentRequest request) {
        return appointmentService.updateAppointment(id, request);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> delete(@PathVariable Long id) {
        return appointmentService.deleteAppointment(id);
    }
}
