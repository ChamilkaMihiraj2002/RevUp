package com.revup.vehicle_service.service;

import com.revup.vehicle_service.exception.DuplicateResourceException;
import com.revup.vehicle_service.exception.ResourceNotFoundException;
import com.revup.vehicle_service.mapper.VehicleMapper;
import com.revup.vehicle_service.model.dto.CreateVehicleRequest;
import com.revup.vehicle_service.model.dto.UpdateVehicleRequest;
import com.revup.vehicle_service.model.dto.VehicleResponse;
import com.revup.vehicle_service.model.entity.Vehicle;
import com.revup.vehicle_service.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class VehicleService {
    
    private final VehicleRepository vehicleRepository;
    private final VehicleMapper vehicleMapper;
    
    @Transactional
    public Mono<VehicleResponse> createVehicle(CreateVehicleRequest request) {
        log.info("Creating vehicle with VIN: {}", request.getVin());
        
        return vehicleRepository.existsByVin(request.getVin())
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new DuplicateResourceException(
                                "Vehicle with VIN " + request.getVin() + " already exists"));
                    }
                    
                    Vehicle vehicle = vehicleMapper.toEntity(request);
                    vehicle.setCreatedAt(LocalDateTime.now());
                    vehicle.setUpdatedAt(LocalDateTime.now());
                    
                    return vehicleRepository.save(vehicle);
                })
                .map(vehicleMapper::toResponse)
                .doOnSuccess(response -> log.info("Vehicle created: {}", response.getId()))
                .doOnError(error -> log.error("Error creating vehicle: {}", error.getMessage()));
    }
    
    public Mono<VehicleResponse> getVehicleById(Long id) {
        return vehicleRepository.findById(id)
                .map(vehicleMapper::toResponse)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Vehicle not found with ID: " + id)));
    }
    
    public Mono<VehicleResponse> getVehicleByVin(String vin) {
        return vehicleRepository.findByVin(vin)
                .map(vehicleMapper::toResponse)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Vehicle not found with VIN: " + vin)));
    }
    
    public Mono<VehicleResponse> getVehicleByLicensePlate(String licensePlate) {
        return vehicleRepository.findByLicensePlate(licensePlate)
                .map(vehicleMapper::toResponse)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Vehicle not found with license plate: " + licensePlate)));
    }
    
    public Flux<VehicleResponse> getVehiclesByCustomerId(Long customerId) {
        return vehicleRepository.findByCustomerId(customerId)
                .map(vehicleMapper::toResponse);
    }
    
    public Flux<VehicleResponse> getAllVehicles() {
        return vehicleRepository.findAll()
                .map(vehicleMapper::toResponse);
    }
    
    public Flux<VehicleResponse> getVehiclesByMakeAndModel(String make, String model) {
        return vehicleRepository.findByMakeAndModel(make, model)
                .map(vehicleMapper::toResponse);
    }
    
    @Transactional
    public Mono<VehicleResponse> updateVehicle(Long id, UpdateVehicleRequest request) {
        log.info("Updating vehicle: {}", id);
        
        return vehicleRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Vehicle not found with ID: " + id)))
                .flatMap(vehicle -> {
                    vehicleMapper.updateEntityFromRequest(request, vehicle);
                    vehicle.setUpdatedAt(LocalDateTime.now());
                    return vehicleRepository.save(vehicle);
                })
                .map(vehicleMapper::toResponse);
    }
    
    @Transactional
    public Mono<VehicleResponse> updateMileage(Long id, Long mileage) {
        return vehicleRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Vehicle not found")))
                .flatMap(vehicle -> {
                    vehicle.setMileage(mileage);
                    vehicle.setUpdatedAt(LocalDateTime.now());
                    return vehicleRepository.save(vehicle);
                })
                .map(vehicleMapper::toResponse);
    }
    
    @Transactional
    public Mono<Void> deleteVehicle(Long id) {
        return vehicleRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Vehicle not found")))
                .flatMap(vehicleRepository::delete);
    }
}
