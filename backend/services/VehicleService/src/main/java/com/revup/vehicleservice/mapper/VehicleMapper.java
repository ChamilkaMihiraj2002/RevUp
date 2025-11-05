package com.revup.vehicleservice.mapper;

import com.revup.vehicleservice.dto.request.CreateVehicleRequest;
import com.revup.vehicleservice.dto.request.UpdateVehicleRequest;
import com.revup.vehicleservice.dto.response.VehicleResponse;
import com.revup.vehicleservice.entity.Vehicle;
import org.springframework.stereotype.Component;

/**
 * Mapper for converting between Vehicle entities and DTOs
 */
@Component
public class VehicleMapper {

    /**
     * Convert CreateVehicleRequest DTO to Vehicle entity
     */
    public Vehicle toEntity(CreateVehicleRequest request) {
        Vehicle vehicle = new Vehicle();
        vehicle.setModel(request.getModel());
        vehicle.setRegistrationNo(request.getRegistrationNo());
        vehicle.setYear(request.getYear());
        vehicle.setColor(request.getColor());
        vehicle.setVehicleType(request.getVehicleType());
        vehicle.setUserId(request.getUserId());
        return vehicle;
    }

    /**
     * Update Vehicle entity from UpdateVehicleRequest DTO
     */
    public void updateEntityFromDto(Vehicle vehicle, UpdateVehicleRequest request) {
        if (request.getModel() != null) {
            vehicle.setModel(request.getModel());
        }
        if (request.getRegistrationNo() != null) {
            vehicle.setRegistrationNo(request.getRegistrationNo());
        }
        if (request.getYear() != null) {
            vehicle.setYear(request.getYear());
        }
        if (request.getColor() != null) {
            vehicle.setColor(request.getColor());
        }
        if (request.getVehicleType() != null) {
            vehicle.setVehicleType(request.getVehicleType());
        }
    }

    /**
     * Convert Vehicle entity to VehicleResponse DTO
     */
    public VehicleResponse toResponse(Vehicle vehicle) {
        return VehicleResponse.builder()
                .vehicleId(vehicle.getVehicleId())
                .model(vehicle.getModel())
                .registrationNo(vehicle.getRegistrationNo())
                .year(vehicle.getYear())
                .color(vehicle.getColor())
                .vehicleType(vehicle.getVehicleType())
                .userId(vehicle.getUserId())
                .build();
    }
}
