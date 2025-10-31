package com.revup.vehicle_service.mapper;

import com.revup.vehicle_service.model.dto.CreateVehicleRequest;
import com.revup.vehicle_service.model.dto.UpdateVehicleRequest;
import com.revup.vehicle_service.model.dto.VehicleResponse;
import com.revup.vehicle_service.model.entity.Vehicle;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-10-31T12:55:10+0530",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.44.0.v20251001-1143, environment: Java 21.0.8 (Eclipse Adoptium)"
)
@Component
public class VehicleMapperImpl implements VehicleMapper {

    @Override
    public Vehicle toEntity(CreateVehicleRequest request) {
        if ( request == null ) {
            return null;
        }

        Vehicle.VehicleBuilder vehicle = Vehicle.builder();

        vehicle.color( request.getColor() );
        vehicle.customerId( request.getCustomerId() );
        vehicle.engineSize( request.getEngineSize() );
        vehicle.fuelType( request.getFuelType() );
        vehicle.licensePlate( request.getLicensePlate() );
        vehicle.make( request.getMake() );
        vehicle.mileage( request.getMileage() );
        vehicle.model( request.getModel() );
        vehicle.notes( request.getNotes() );
        vehicle.transmissionType( request.getTransmissionType() );
        vehicle.vin( request.getVin() );
        vehicle.year( request.getYear() );

        return vehicle.build();
    }

    @Override
    public VehicleResponse toResponse(Vehicle vehicle) {
        if ( vehicle == null ) {
            return null;
        }

        VehicleResponse.VehicleResponseBuilder vehicleResponse = VehicleResponse.builder();

        vehicleResponse.color( vehicle.getColor() );
        vehicleResponse.createdAt( vehicle.getCreatedAt() );
        vehicleResponse.customerId( vehicle.getCustomerId() );
        vehicleResponse.engineSize( vehicle.getEngineSize() );
        vehicleResponse.fuelType( vehicle.getFuelType() );
        vehicleResponse.id( vehicle.getId() );
        vehicleResponse.licensePlate( vehicle.getLicensePlate() );
        vehicleResponse.make( vehicle.getMake() );
        vehicleResponse.mileage( vehicle.getMileage() );
        vehicleResponse.model( vehicle.getModel() );
        vehicleResponse.notes( vehicle.getNotes() );
        vehicleResponse.transmissionType( vehicle.getTransmissionType() );
        vehicleResponse.updatedAt( vehicle.getUpdatedAt() );
        vehicleResponse.vin( vehicle.getVin() );
        vehicleResponse.year( vehicle.getYear() );

        return vehicleResponse.build();
    }

    @Override
    public void updateEntityFromRequest(UpdateVehicleRequest request, Vehicle vehicle) {
        if ( request == null ) {
            return;
        }

        if ( request.getColor() != null ) {
            vehicle.setColor( request.getColor() );
        }
        if ( request.getEngineSize() != null ) {
            vehicle.setEngineSize( request.getEngineSize() );
        }
        if ( request.getFuelType() != null ) {
            vehicle.setFuelType( request.getFuelType() );
        }
        if ( request.getLicensePlate() != null ) {
            vehicle.setLicensePlate( request.getLicensePlate() );
        }
        if ( request.getMileage() != null ) {
            vehicle.setMileage( request.getMileage() );
        }
        if ( request.getNotes() != null ) {
            vehicle.setNotes( request.getNotes() );
        }
        if ( request.getTransmissionType() != null ) {
            vehicle.setTransmissionType( request.getTransmissionType() );
        }
    }
}
