package com.revup.vehicleservice.controller;

import com.revup.vehicleservice.dto.request.CreateVehicleRequest;
import com.revup.vehicleservice.dto.request.UpdateVehicleRequest;
import com.revup.vehicleservice.dto.response.VehicleResponse;
import com.revup.vehicleservice.service.VehicleService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api/v1/vehicles")
public class VehicleController {
    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @PostMapping
    public Mono<ResponseEntity<VehicleResponse>> createVehicle(@Valid @RequestBody CreateVehicleRequest request) {
        log.info("Creating new vehicle with registration number: {}", request.getRegistrationNo());
        return vehicleService.createVehicle(request)
                .map(vehicle -> new ResponseEntity<>(vehicle, HttpStatus.CREATED))
                .doOnSuccess(vehicle -> log.info("Vehicle created successfully with id: {}", vehicle.getBody().getVehicleId()));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<VehicleResponse>> getVehicle(@PathVariable Long id) {
        log.info("Fetching vehicle with id: {}", id);
        return vehicleService.getVehicleById(id)
                .map(ResponseEntity::ok);
    }

    @GetMapping
    public Flux<VehicleResponse> getAllVehicles() {
        log.info("Fetching all vehicles");
        return vehicleService.getAllVehicles();
    }

    @GetMapping("/user/{userId}")
    public Flux<VehicleResponse> getVehiclesByUserId(@PathVariable Long userId) {
        log.info("Fetching all vehicles for user with id: {}", userId);
        return vehicleService.getVehiclesByUserId(userId);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<VehicleResponse>> updateVehicle(
            @PathVariable Long id,
            @Valid @RequestBody UpdateVehicleRequest request) {
        log.info("Updating vehicle with id: {}", id);
        return vehicleService.updateVehicle(id, request)
                .map(ResponseEntity::ok)
                .doOnSuccess(vehicle -> log.info("Vehicle updated successfully with id: {}", id));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteVehicle(@PathVariable Long id) {
        log.info("Deleting vehicle with id: {}", id);
        return vehicleService.deleteVehicle(id)
                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)))
                .doOnSuccess(response -> log.info("Vehicle deleted successfully with id: {}", id));
    }

    @DeleteMapping("/user/{userId}")
    public Mono<ResponseEntity<Void>> deleteVehiclesByUserId(@PathVariable Long userId) {
        log.info("Deleting all vehicles for user with id: {}", userId);
        return vehicleService.deleteVehiclesByUserId(userId)
                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)))
                .doOnSuccess(response -> log.info("All vehicles deleted successfully for user with id: {}", userId));
    }
}
