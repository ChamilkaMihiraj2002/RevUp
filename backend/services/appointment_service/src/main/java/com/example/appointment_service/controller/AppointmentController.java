package com.example.appointment_service.controller;

import com.example.appointment_service.dto.*;
import com.example.appointment_service.service.AppointmentServiceInterface;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalTime;

@RestController
@RequestMapping("/api/v1/appointments")
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

    @GetMapping("/customer/{customerId}")
    public Flux<AppointmentResponse> getByCustomerId(@PathVariable Long customerId) {
        return appointmentService.getAppointmentsByCustomerId(customerId);
    }

    @GetMapping("/vehicle/{vehicleId}")
    public Flux<AppointmentResponse> getByVehicleId(@PathVariable Long vehicleId) {
        return appointmentService.getAppointmentsByVehicleId(vehicleId);
    }

    @GetMapping("/technician/{technicianId}")
    public Flux<AppointmentResponse> getByTechnicianId(@PathVariable Long technicianId) {
        return appointmentService.getAppointmentsByTechnicianId(technicianId);
    }

    @GetMapping("/status/{status}")
    public Flux<AppointmentResponse> getByStatus(@PathVariable String status) {
        return appointmentService.getAppointmentsByStatus(status);
    }

    @GetMapping("/unassigned")
    public Flux<AppointmentResponse> getUnassigned() {
        return appointmentService.getUnassignedAppointments();
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

    @GetMapping("/slots/availability")
    public Mono<SlotAvailabilityResponse> checkSlotAvailability(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime time) {
        return appointmentService.checkSlotAvailability(date, time);
    }
}
