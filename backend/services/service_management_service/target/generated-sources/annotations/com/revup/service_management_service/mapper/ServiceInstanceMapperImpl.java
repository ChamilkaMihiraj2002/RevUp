package com.revup.service_management_service.mapper;

import com.revup.service_management_service.dto.CreateServiceInstanceRequest;
import com.revup.service_management_service.dto.ServiceInstanceResponse;
import com.revup.service_management_service.dto.UpdateServiceInstanceRequest;
import com.revup.service_management_service.model.ServiceInstance;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-10-31T12:55:02+0530",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.44.0.v20251001-1143, environment: Java 21.0.8 (Eclipse Adoptium)"
)
@Component
public class ServiceInstanceMapperImpl implements ServiceInstanceMapper {

    @Override
    public ServiceInstanceResponse toResponse(ServiceInstance serviceInstance) {
        if ( serviceInstance == null ) {
            return null;
        }

        ServiceInstanceResponse serviceInstanceResponse = new ServiceInstanceResponse();

        serviceInstanceResponse.setActualDuration( serviceInstance.getActualDuration() );
        serviceInstanceResponse.setActualPrice( serviceInstance.getActualPrice() );
        serviceInstanceResponse.setAppointmentId( serviceInstance.getAppointmentId() );
        serviceInstanceResponse.setCreatedAt( serviceInstance.getCreatedAt() );
        serviceInstanceResponse.setEmployeeId( serviceInstance.getEmployeeId() );
        serviceInstanceResponse.setEndDate( serviceInstance.getEndDate() );
        serviceInstanceResponse.setId( serviceInstance.getId() );
        serviceInstanceResponse.setNotes( serviceInstance.getNotes() );
        serviceInstanceResponse.setProgressPercentage( serviceInstance.getProgressPercentage() );
        serviceInstanceResponse.setServiceCatalogId( serviceInstance.getServiceCatalogId() );
        serviceInstanceResponse.setStartDate( serviceInstance.getStartDate() );
        serviceInstanceResponse.setStatus( serviceInstance.getStatus() );
        serviceInstanceResponse.setUpdatedAt( serviceInstance.getUpdatedAt() );

        return serviceInstanceResponse;
    }

    @Override
    public ServiceInstance toEntity(CreateServiceInstanceRequest request) {
        if ( request == null ) {
            return null;
        }

        ServiceInstance serviceInstance = new ServiceInstance();

        serviceInstance.setActualPrice( request.getActualPrice() );
        serviceInstance.setAppointmentId( request.getAppointmentId() );
        serviceInstance.setEmployeeId( request.getEmployeeId() );
        serviceInstance.setNotes( request.getNotes() );
        serviceInstance.setProgressPercentage( request.getProgressPercentage() );
        serviceInstance.setServiceCatalogId( request.getServiceCatalogId() );
        serviceInstance.setStartDate( request.getStartDate() );
        serviceInstance.setStatus( request.getStatus() );

        return serviceInstance;
    }

    @Override
    public void updateServiceInstanceFromRequest(UpdateServiceInstanceRequest request, ServiceInstance serviceInstance) {
        if ( request == null ) {
            return;
        }

        if ( request.getActualDuration() != null ) {
            serviceInstance.setActualDuration( request.getActualDuration() );
        }
        if ( request.getActualPrice() != null ) {
            serviceInstance.setActualPrice( request.getActualPrice() );
        }
        if ( request.getEmployeeId() != null ) {
            serviceInstance.setEmployeeId( request.getEmployeeId() );
        }
        if ( request.getEndDate() != null ) {
            serviceInstance.setEndDate( request.getEndDate() );
        }
        if ( request.getNotes() != null ) {
            serviceInstance.setNotes( request.getNotes() );
        }
        if ( request.getProgressPercentage() != null ) {
            serviceInstance.setProgressPercentage( request.getProgressPercentage() );
        }
        if ( request.getStartDate() != null ) {
            serviceInstance.setStartDate( request.getStartDate() );
        }
        if ( request.getStatus() != null ) {
            serviceInstance.setStatus( request.getStatus() );
        }
    }
}
