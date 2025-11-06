package com.example.appointment_service.controller;

import com.example.appointment_service.dto.CreateAppointmentServiceRequest;
import com.example.appointment_service.dto.AppointmentServiceResponse;
import com.example.appointment_service.service.AppointmentServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/appointment-services")
@RequiredArgsConstructor
public class AppointmentServiceController {

    private final AppointmentServiceService appointmentServiceService;

    @PostMapping
    public Mono<ResponseEntity<AppointmentServiceResponse>> create(@RequestBody CreateAppointmentServiceRequest request) {
        return appointmentServiceService.create(request)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<AppointmentServiceResponse>> getById(@PathVariable Long id) {
        return appointmentServiceService.getById(id)
                .map(ResponseEntity::ok);
    }

    @GetMapping
    public Mono<ResponseEntity<List<AppointmentServiceResponse>>> getAll() {
        return appointmentServiceService.getAll()
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteById(@PathVariable Long id) {
        return appointmentServiceService.deleteById(id)
                .thenReturn(ResponseEntity.noContent().build());
    }
}
