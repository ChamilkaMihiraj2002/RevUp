package com.revup.service_management_service.controller;

import com.revup.service_management_service.dto.CreateServiceCatalogRequest;
import com.revup.service_management_service.dto.ServiceCatalogResponse;
import com.revup.service_management_service.service.ServiceCatalogService;
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
@RequestMapping("/api/services/catalog")
@RequiredArgsConstructor
@Tag(name = "Service Catalog Management", description = "APIs for managing service catalog")
public class ServiceCatalogController {

    private final ServiceCatalogService serviceCatalogService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create service catalog", description = "Creates a new service in the catalog")
    public Mono<ServiceCatalogResponse> createServiceCatalog(@Valid @RequestBody CreateServiceCatalogRequest request) {
        return serviceCatalogService.createServiceCatalog(request);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get service catalog by ID")
    public Mono<ServiceCatalogResponse> getServiceCatalogById(
            @Parameter(description = "Service catalog ID") @PathVariable Long id) {
        return serviceCatalogService.getServiceCatalogById(id);
    }

    @GetMapping
    @Operation(summary = "Get all service catalogs")
    public Flux<ServiceCatalogResponse> getAllServiceCatalogs() {
        return serviceCatalogService.getAllServiceCatalogs();
    }

    @GetMapping("/active")
    @Operation(summary = "Get active service catalogs")
    public Flux<ServiceCatalogResponse> getActiveServiceCatalogs() {
        return serviceCatalogService.getActiveServiceCatalogs();
    }

    @GetMapping("/category/{category}")
    @Operation(summary = "Get services by category")
    public Flux<ServiceCatalogResponse> getServicesByCategory(
            @Parameter(description = "Service category") @PathVariable String category) {
        return serviceCatalogService.getServiceCatalogsByCategory(category);
    }

    @GetMapping("/search")
    @Operation(summary = "Search services by name")
    public Flux<ServiceCatalogResponse> searchServices(
            @Parameter(description = "Search term") @RequestParam String name) {
        return serviceCatalogService.searchServiceCatalogsByName(name);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update service catalog")
    public Mono<ServiceCatalogResponse> updateServiceCatalog(
            @Parameter(description = "Service catalog ID") @PathVariable Long id,
            @Valid @RequestBody CreateServiceCatalogRequest request) {
        return serviceCatalogService.updateServiceCatalog(id, request);
    }

    @PatchMapping("/{id}/toggle-active")
    @Operation(summary = "Toggle active status")
    public Mono<ServiceCatalogResponse> toggleActiveStatus(
            @Parameter(description = "Service catalog ID") @PathVariable Long id) {
        return serviceCatalogService.toggleActiveStatus(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete service catalog")
    public Mono<Void> deleteServiceCatalog(
            @Parameter(description = "Service catalog ID") @PathVariable Long id) {
        return serviceCatalogService.deleteServiceCatalog(id);
    }
}
