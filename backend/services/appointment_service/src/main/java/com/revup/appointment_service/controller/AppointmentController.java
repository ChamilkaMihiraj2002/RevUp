package com.revup.appointment_service.controller;

import com.revup.appointment_service.dto.AppointmentResponse;
import com.revup.appointment_service.dto.CreateAppointmentRequest;
import com.revup.appointment_service.dto.UpdateAppointmentRequest;
import com.revup.appointment_service.model.AppointmentStatus;
import com.revup.appointment_service.service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
@Tag(name = "Appointment Management", description = "APIs for managing appointments and scheduling")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create new appointment", description = "Books a new appointment for a customer")
    public Mono<AppointmentResponse> createAppointment(@Valid @RequestBody CreateAppointmentRequest request) {
        return appointmentService.createAppointment(request);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get appointment by ID", description = "Retrieves an appointment by its ID")
    public Mono<AppointmentResponse> getAppointmentById(
            @Parameter(description = "Appointment ID") @PathVariable Long id) {
        return appointmentService.getAppointmentById(id);
    }

    @GetMapping
    @Operation(summary = "Get all appointments", description = "Retrieves all appointments")
    public Flux<AppointmentResponse> getAllAppointments() {
        return appointmentService.getAllAppointments();
    }

    @GetMapping("/customer/{customerId}")
    @Operation(summary = "Get appointments by customer", description = "Retrieves all appointments for a specific customer")
    public Flux<AppointmentResponse> getAppointmentsByCustomerId(
            @Parameter(description = "Customer ID") @PathVariable Long customerId) {
        return appointmentService.getAppointmentsByCustomerId(customerId);
    }

    @GetMapping("/vehicle/{vehicleId}")
    @Operation(summary = "Get appointments by vehicle", description = "Retrieves all appointments for a specific vehicle")
    public Flux<AppointmentResponse> getAppointmentsByVehicleId(
            @Parameter(description = "Vehicle ID") @PathVariable Long vehicleId) {
        return appointmentService.getAppointmentsByVehicleId(vehicleId);
    }

    @GetMapping("/employee/{employeeId}")
    @Operation(summary = "Get appointments by employee", description = "Retrieves all appointments assigned to a specific employee")
    public Flux<AppointmentResponse> getAppointmentsByEmployeeId(
            @Parameter(description = "Employee ID") @PathVariable Long employeeId) {
        return appointmentService.getAppointmentsByEmployeeId(employeeId);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get appointments by status", description = "Retrieves appointments with a specific status")
    public Flux<AppointmentResponse> getAppointmentsByStatus(
            @Parameter(description = "Appointment status") @PathVariable AppointmentStatus status) {
        return appointmentService.getAppointmentsByStatus(status);
    }

    @GetMapping("/upcoming")
    @Operation(summary = "Get upcoming appointments", description = "Retrieves upcoming appointments within specified days")
    public Flux<AppointmentResponse> getUpcomingAppointments(
            @Parameter(description = "Number of days ahead") @RequestParam(defaultValue = "7") int daysAhead) {
        return appointmentService.getUpcomingAppointments(daysAhead);
    }

    @GetMapping("/pending")
    @Operation(summary = "Get pending appointments", description = "Retrieves pending appointments")
    public Flux<AppointmentResponse> getPendingAppointments(
            @Parameter(description = "Maximum number of results") @RequestParam(defaultValue = "50") int limit) {
        return appointmentService.getPendingAppointments(limit);
    }

    @GetMapping("/employee/{employeeId}/schedule")
    @Operation(summary = "Get employee schedule", description = "Retrieves employee's schedule for a date range")
    public Flux<AppointmentResponse> getEmployeeSchedule(
            @Parameter(description = "Employee ID") @PathVariable Long employeeId,
            @Parameter(description = "Start date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "End date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return appointmentService.getEmployeeSchedule(employeeId, startDate, endDate);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update appointment", description = "Updates an appointment's details")
    public Mono<AppointmentResponse> updateAppointment(
            @Parameter(description = "Appointment ID") @PathVariable Long id,
            @Valid @RequestBody UpdateAppointmentRequest request) {
        return appointmentService.updateAppointment(id, request);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update appointment status", description = "Updates an appointment's status")
    public Mono<AppointmentResponse> updateAppointmentStatus(
            @Parameter(description = "Appointment ID") @PathVariable Long id,
            @Parameter(description = "New status") @RequestParam AppointmentStatus status) {
        return appointmentService.updateAppointmentStatus(id, status);
    }

    @PatchMapping("/{id}/assign")
    @Operation(summary = "Assign employee", description = "Assigns an employee to an appointment")
    public Mono<AppointmentResponse> assignEmployee(
            @Parameter(description = "Appointment ID") @PathVariable Long id,
            @Parameter(description = "Employee ID") @RequestParam Long employeeId) {
        return appointmentService.assignEmployee(id, employeeId);
    }

    @PostMapping("/{id}/confirm")
    @Operation(summary = "Confirm appointment", description = "Confirms a pending appointment")
    public Mono<AppointmentResponse> confirmAppointment(
            @Parameter(description = "Appointment ID") @PathVariable Long id) {
        return appointmentService.confirmAppointment(id);
    }

    @PostMapping("/{id}/cancel")
    @Operation(summary = "Cancel appointment", description = "Cancels an appointment")
    public Mono<AppointmentResponse> cancelAppointment(
            @Parameter(description = "Appointment ID") @PathVariable Long id) {
        return appointmentService.cancelAppointment(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete appointment", description = "Deletes an appointment")
    public Mono<Void> deleteAppointment(
            @Parameter(description = "Appointment ID") @PathVariable Long id) {
        return appointmentService.deleteAppointment(id);
    }
}
