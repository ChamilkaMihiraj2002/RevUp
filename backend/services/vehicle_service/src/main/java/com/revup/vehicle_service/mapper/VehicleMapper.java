package com.revup.vehicle_service.mapper;

import com.revup.vehicle_service.model.dto.CreateVehicleRequest;
import com.revup.vehicle_service.model.dto.UpdateVehicleRequest;
import com.revup.vehicle_service.model.dto.VehicleResponse;
import com.revup.vehicle_service.model.entity.Vehicle;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface VehicleMapper {
    
    Vehicle toEntity(CreateVehicleRequest request);
    
    VehicleResponse toResponse(Vehicle vehicle);
    
    void updateEntityFromRequest(UpdateVehicleRequest request, @MappingTarget Vehicle vehicle);
}
