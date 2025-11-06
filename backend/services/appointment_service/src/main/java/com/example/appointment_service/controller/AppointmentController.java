package com.example.appointment_service.controller;

import com.example.appointment_service.dto.*;
import com.example.appointment_service.service.AppointmentServiceInterface;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentServiceInterface appointmentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<AppointmentResponse> create(@Valid @RequestBody CreateAppointmentRequest request) {
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
    public Mono<AppointmentResponse> update(@PathVariable Long id, @Valid @RequestBody UpdateAppointmentRequest request) {
        return appointmentService.updateAppointment(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable Long id) {
        return appointmentService.deleteAppointment(id);
    }
}
