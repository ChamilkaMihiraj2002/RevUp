package com.revup.vehicleservice.service;

import com.revup.vehicleservice.dto.request.CreateVehicleRequest;
import com.revup.vehicleservice.dto.request.UpdateVehicleRequest;
import com.revup.vehicleservice.dto.response.VehicleResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface VehicleService {
    Mono<VehicleResponse> createVehicle(CreateVehicleRequest request);
    Mono<VehicleResponse> getVehicleById(Long id);
    Flux<VehicleResponse> getAllVehicles();
    Flux<VehicleResponse> getVehiclesByUserId(Long userId);
    Mono<VehicleResponse> updateVehicle(Long id, UpdateVehicleRequest request);
    Mono<Void> deleteVehicle(Long id);
    Mono<Void> deleteVehiclesByUserId(Long userId);
}
