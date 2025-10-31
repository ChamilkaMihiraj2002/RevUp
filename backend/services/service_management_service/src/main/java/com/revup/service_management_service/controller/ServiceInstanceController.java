package com.revup.service_management_service.controller;

import com.revup.service_management_service.dto.*;
import com.revup.service_management_service.model.ServiceStatus;
import com.revup.service_management_service.service.ServiceInstanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/services/instances")
@RequiredArgsConstructor
@Tag(name = "Service Instance Management", description = "APIs for managing service instances and tracking progress")
public class ServiceInstanceController {

    private final ServiceInstanceService serviceInstanceService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create service instance", description = "Creates a new service instance for an appointment")
    public Mono<ServiceInstanceResponse> createServiceInstance(@Valid @RequestBody CreateServiceInstanceRequest request) {
        return serviceInstanceService.createServiceInstance(request);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get service instance by ID")
    public Mono<ServiceInstanceResponse> getServiceInstanceById(
            @Parameter(description = "Service instance ID") @PathVariable Long id) {
        return serviceInstanceService.getServiceInstanceById(id);
    }

    @GetMapping
    @Operation(summary = "Get all service instances")
    public Flux<ServiceInstanceResponse> getAllServiceInstances() {
        return serviceInstanceService.getAllServiceInstances();
    }

    @GetMapping("/appointment/{appointmentId}")
    @Operation(summary = "Get service instances by appointment")
    public Flux<ServiceInstanceResponse> getServiceInstancesByAppointment(
            @Parameter(description = "Appointment ID") @PathVariable Long appointmentId) {
        return serviceInstanceService.getServiceInstancesByAppointment(appointmentId);
    }

    @GetMapping("/appointment/{appointmentId}/progress")
    @Operation(summary = "Get overall progress for appointment")
    public Mono<Double> getOverallProgress(
            @Parameter(description = "Appointment ID") @PathVariable Long appointmentId) {
        return serviceInstanceService.getOverallProgress(appointmentId);
    }

    @GetMapping("/employee/{employeeId}")
    @Operation(summary = "Get service instances by employee")
    public Flux<ServiceInstanceResponse> getServiceInstancesByEmployee(
            @Parameter(description = "Employee ID") @PathVariable Long employeeId) {
        return serviceInstanceService.getServiceInstancesByEmployee(employeeId);
    }

    @GetMapping("/employee/{employeeId}/completed-count")
    @Operation(summary = "Get completed services count for employee")
    public Mono<Long> getCompletedServicesCount(
            @Parameter(description = "Employee ID") @PathVariable Long employeeId) {
        return serviceInstanceService.getCompletedServicesCount(employeeId);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get service instances by status")
    public Flux<ServiceInstanceResponse> getServiceInstancesByStatus(
            @Parameter(description = "Service status") @PathVariable ServiceStatus status) {
        return serviceInstanceService.getServiceInstancesByStatus(status);
    }

    @GetMapping("/in-progress")
    @Operation(summary = "Get all in-progress services")
    public Flux<ServiceInstanceResponse> getInProgressServices() {
        return serviceInstanceService.getInProgressServices();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update service instance")
    public Mono<ServiceInstanceResponse> updateServiceInstance(
            @Parameter(description = "Service instance ID") @PathVariable Long id,
            @Valid @RequestBody UpdateServiceInstanceRequest request) {
        return serviceInstanceService.updateServiceInstance(id, request);
    }

    @PostMapping("/progress")
    @Operation(summary = "Update progress", description = "Updates progress percentage for a service instance")
    public Mono<ServiceInstanceResponse> updateProgress(@Valid @RequestBody ProgressUpdateRequest request) {
        return serviceInstanceService.updateProgress(request);
    }

    @PostMapping("/{id}/start")
    @Operation(summary = "Start service", description = "Marks service as in-progress")
    public Mono<ServiceInstanceResponse> startService(
            @Parameter(description = "Service instance ID") @PathVariable Long id) {
        return serviceInstanceService.startService(id);
    }

    @PostMapping("/{id}/complete")
    @Operation(summary = "Complete service", description = "Marks service as completed")
    public Mono<ServiceInstanceResponse> completeService(
            @Parameter(description = "Service instance ID") @PathVariable Long id) {
        return serviceInstanceService.completeService(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete service instance")
    public Mono<Void> deleteServiceInstance(
            @Parameter(description = "Service instance ID") @PathVariable Long id) {
        return serviceInstanceService.deleteServiceInstance(id);
    }
}
