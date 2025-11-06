package com.example.appointment_service.controller;

import com.example.appointment_service.dto.CreateServiceTypeRequest;
import com.example.appointment_service.dto.ServiceTypeResponse;
import com.example.appointment_service.dto.UpdateServiceTypeRequest;
import com.example.appointment_service.service.ServiceTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/v1/service-types")
@RequiredArgsConstructor
public class ServiceTypeController {

    private final ServiceTypeService serviceTypeService;

    @PostMapping
    public Mono<ResponseEntity<ServiceTypeResponse>> create(@RequestBody CreateServiceTypeRequest request) {
        return serviceTypeService.create(request)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<ServiceTypeResponse>> getById(@PathVariable Long id) {
        return serviceTypeService.getById(id)
                .map(ResponseEntity::ok);
    }

    @GetMapping
    public Mono<ResponseEntity<List<ServiceTypeResponse>>> getAll() {
        return serviceTypeService.getAll()
                .map(ResponseEntity::ok);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<ServiceTypeResponse>> update(
            @PathVariable Long id,
            @RequestBody UpdateServiceTypeRequest request) {
        return serviceTypeService.update(id, request)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteById(@PathVariable Long id) {
        return serviceTypeService.deleteById(id)
                .thenReturn(ResponseEntity.noContent().build());
    }
}
