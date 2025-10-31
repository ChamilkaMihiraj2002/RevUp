package com.revup.vehicle_service.controller;

import com.revup.vehicle_service.model.dto.CreateVehicleRequest;
import com.revup.vehicle_service.model.dto.UpdateVehicleRequest;
import com.revup.vehicle_service.model.dto.VehicleResponse;
import com.revup.vehicle_service.service.VehicleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/vehicles")
@RequiredArgsConstructor
@Tag(name = "Vehicle Management", description = "APIs for managing vehicles")
public class VehicleController {
    
    private final VehicleService vehicleService;
    
    @PostMapping
    @Operation(summary = "Create vehicle", description = "Register a new vehicle")
    public Mono<ResponseEntity<VehicleResponse>> createVehicle(@Valid @RequestBody CreateVehicleRequest request) {
        return vehicleService.createVehicle(request)
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response));
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get vehicle by ID")
    public Mono<ResponseEntity<VehicleResponse>> getVehicleById(@PathVariable Long id) {
        return vehicleService.getVehicleById(id)
                .map(ResponseEntity::ok);
    }
    
    @GetMapping("/vin/{vin}")
    @Operation(summary = "Get vehicle by VIN")
    public Mono<ResponseEntity<VehicleResponse>> getVehicleByVin(@PathVariable String vin) {
        return vehicleService.getVehicleByVin(vin)
                .map(ResponseEntity::ok);
    }
    
    @GetMapping("/license/{licensePlate}")
    @Operation(summary = "Get vehicle by license plate")
    public Mono<ResponseEntity<VehicleResponse>> getVehicleByLicensePlate(@PathVariable String licensePlate) {
        return vehicleService.getVehicleByLicensePlate(licensePlate)
                .map(ResponseEntity::ok);
    }
    
    @GetMapping("/customer/{customerId}")
    @Operation(summary = "Get vehicles by customer ID")
    public Flux<VehicleResponse> getVehiclesByCustomerId(@PathVariable Long customerId) {
        return vehicleService.getVehiclesByCustomerId(customerId);
    }
    
    @GetMapping
    @Operation(summary = "Get all vehicles")
    public Flux<VehicleResponse> getAllVehicles() {
        return vehicleService.getAllVehicles();
    }
    
    @GetMapping("/search")
    @Operation(summary = "Search vehicles by make and model")
    public Flux<VehicleResponse> searchVehicles(
            @RequestParam String make,
            @RequestParam String model) {
        return vehicleService.getVehiclesByMakeAndModel(make, model);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update vehicle")
    public Mono<ResponseEntity<VehicleResponse>> updateVehicle(
            @PathVariable Long id,
            @Valid @RequestBody UpdateVehicleRequest request) {
        return vehicleService.updateVehicle(id, request)
                .map(ResponseEntity::ok);
    }
    
    @PatchMapping("/{id}/mileage")
    @Operation(summary = "Update vehicle mileage")
    public Mono<ResponseEntity<VehicleResponse>> updateMileage(
            @PathVariable Long id,
            @RequestParam Long mileage) {
        return vehicleService.updateMileage(id, mileage)
                .map(ResponseEntity::ok);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete vehicle")
    public Mono<ResponseEntity<Void>> deleteVehicle(@PathVariable Long id) {
        return vehicleService.deleteVehicle(id)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()));
    }
}
