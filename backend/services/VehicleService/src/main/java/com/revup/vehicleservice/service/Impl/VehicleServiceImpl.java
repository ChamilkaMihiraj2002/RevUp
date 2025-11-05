package com.revup.vehicleservice.service.Impl;

import com.revup.vehicleservice.dto.request.CreateVehicleRequest;
import com.revup.vehicleservice.dto.request.UpdateVehicleRequest;
import com.revup.vehicleservice.dto.response.VehicleResponse;
import com.revup.vehicleservice.entity.Vehicle;
import com.revup.vehicleservice.exception.ResourceNotFoundException;
import com.revup.vehicleservice.mapper.VehicleMapper;
import com.revup.vehicleservice.repository.VehicleRepository;
import com.revup.vehicleservice.service.VehicleService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

@Service
public class VehicleServiceImpl implements VehicleService {
    private final VehicleRepository vehicleRepository;
    private final Scheduler jdbcScheduler;
    private final VehicleMapper vehicleMapper;

    public VehicleServiceImpl(VehicleRepository vehicleRepository, Scheduler jdbcScheduler, VehicleMapper vehicleMapper) {
        this.vehicleRepository = vehicleRepository;
        this.jdbcScheduler = jdbcScheduler;
        this.vehicleMapper = vehicleMapper;
    }

    @Override
    public Mono<VehicleResponse> createVehicle(CreateVehicleRequest request) {
        return Mono.fromCallable(() -> {
                    // Check if registration number already exists
                    if (vehicleRepository.existsByRegistrationNo(request.getRegistrationNo())) {
                        throw new IllegalArgumentException("Vehicle with registration number " + request.getRegistrationNo() + " already exists");
                    }
                    
                    Vehicle vehicle = vehicleMapper.toEntity(request);
                    Vehicle savedVehicle = vehicleRepository.save(vehicle);
                    return vehicleMapper.toResponse(savedVehicle);
                })
                .subscribeOn(jdbcScheduler);
    }

    @Override
    public Mono<VehicleResponse> getVehicleById(Long id) {
        return Mono.fromCallable(() -> vehicleRepository.findById(id))
                .subscribeOn(jdbcScheduler)
                .flatMap(optionalVehicle -> optionalVehicle
                        .map(vehicle -> Mono.just(vehicleMapper.toResponse(vehicle)))
                        .orElseGet(() -> Mono.error(new ResourceNotFoundException("Vehicle not found with id: " + id))));
    }

    @Override
    public Flux<VehicleResponse> getAllVehicles() {
        return Mono.fromCallable(() -> vehicleRepository.findAll())
                .subscribeOn(jdbcScheduler)
                .flatMapMany(Flux::fromIterable)
                .map(vehicleMapper::toResponse);
    }

    @Override
    public Flux<VehicleResponse> getVehiclesByUserId(Long userId) {
        return Mono.fromCallable(() -> vehicleRepository.findByUserId(userId))
                .subscribeOn(jdbcScheduler)
                .flatMapMany(Flux::fromIterable)
                .map(vehicleMapper::toResponse);
    }

    @Override
    public Mono<VehicleResponse> updateVehicle(Long id, UpdateVehicleRequest request) {
        return Mono.fromCallable(() -> {
                    Vehicle vehicle = vehicleRepository.findById(id)
                            .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with id: " + id));
                    
                    // Check if new registration number already exists (excluding current vehicle)
                    if (request.getRegistrationNo() != null && 
                        !request.getRegistrationNo().equals(vehicle.getRegistrationNo()) &&
                        vehicleRepository.existsByRegistrationNo(request.getRegistrationNo())) {
                        throw new IllegalArgumentException("Vehicle with registration number " + request.getRegistrationNo() + " already exists");
                    }
                    
                    vehicleMapper.updateEntityFromDto(vehicle, request);
                    Vehicle updatedVehicle = vehicleRepository.save(vehicle);
                    return vehicleMapper.toResponse(updatedVehicle);
                })
                .subscribeOn(jdbcScheduler);
    }

    @Override
    public Mono<Void> deleteVehicle(Long id) {
        return Mono.fromRunnable(() -> {
                    if (!vehicleRepository.existsById(id)) {
                        throw new ResourceNotFoundException("Vehicle not found with id: " + id);
                    }
                    vehicleRepository.deleteById(id);
                })
                .subscribeOn(jdbcScheduler)
                .then();
    }
}
