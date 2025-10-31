package com.revup.vehicle_service.repository;

import com.revup.vehicle_service.model.entity.Vehicle;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface VehicleRepository extends R2dbcRepository<Vehicle, Long> {
    
    Flux<Vehicle> findByCustomerId(Long customerId);
    
    Mono<Vehicle> findByVin(String vin);
    
    Mono<Vehicle> findByLicensePlate(String licensePlate);
    
    Mono<Boolean> existsByVin(String vin);
    
    Mono<Boolean> existsByLicensePlate(String licensePlate);
    
    Flux<Vehicle> findByMakeAndModel(String make, String model);
}
