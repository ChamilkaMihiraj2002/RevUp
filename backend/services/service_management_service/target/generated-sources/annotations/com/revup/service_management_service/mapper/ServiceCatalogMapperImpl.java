package com.revup.service_management_service.mapper;

import com.revup.service_management_service.dto.CreateServiceCatalogRequest;
import com.revup.service_management_service.dto.ServiceCatalogResponse;
import com.revup.service_management_service.model.ServiceCatalog;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-10-31T12:55:02+0530",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.44.0.v20251001-1143, environment: Java 21.0.8 (Eclipse Adoptium)"
)
@Component
public class ServiceCatalogMapperImpl implements ServiceCatalogMapper {

    @Override
    public ServiceCatalogResponse toResponse(ServiceCatalog serviceCatalog) {
        if ( serviceCatalog == null ) {
            return null;
        }

        ServiceCatalogResponse serviceCatalogResponse = new ServiceCatalogResponse();

        serviceCatalogResponse.setBasePrice( serviceCatalog.getBasePrice() );
        serviceCatalogResponse.setCategory( serviceCatalog.getCategory() );
        serviceCatalogResponse.setCreatedAt( serviceCatalog.getCreatedAt() );
        serviceCatalogResponse.setDescription( serviceCatalog.getDescription() );
        serviceCatalogResponse.setEstimatedDuration( serviceCatalog.getEstimatedDuration() );
        serviceCatalogResponse.setId( serviceCatalog.getId() );
        serviceCatalogResponse.setIsActive( serviceCatalog.getIsActive() );
        serviceCatalogResponse.setName( serviceCatalog.getName() );
        serviceCatalogResponse.setUpdatedAt( serviceCatalog.getUpdatedAt() );

        return serviceCatalogResponse;
    }

    @Override
    public ServiceCatalog toEntity(CreateServiceCatalogRequest request) {
        if ( request == null ) {
            return null;
        }

        ServiceCatalog serviceCatalog = new ServiceCatalog();

        serviceCatalog.setBasePrice( request.getBasePrice() );
        serviceCatalog.setCategory( request.getCategory() );
        serviceCatalog.setDescription( request.getDescription() );
        serviceCatalog.setEstimatedDuration( request.getEstimatedDuration() );
        serviceCatalog.setIsActive( request.getIsActive() );
        serviceCatalog.setName( request.getName() );

        return serviceCatalog;
    }

    @Override
    public void updateServiceCatalogFromRequest(CreateServiceCatalogRequest request, ServiceCatalog serviceCatalog) {
        if ( request == null ) {
            return;
        }

        if ( request.getBasePrice() != null ) {
            serviceCatalog.setBasePrice( request.getBasePrice() );
        }
        if ( request.getCategory() != null ) {
            serviceCatalog.setCategory( request.getCategory() );
        }
        if ( request.getDescription() != null ) {
            serviceCatalog.setDescription( request.getDescription() );
        }
        if ( request.getEstimatedDuration() != null ) {
            serviceCatalog.setEstimatedDuration( request.getEstimatedDuration() );
        }
        if ( request.getIsActive() != null ) {
            serviceCatalog.setIsActive( request.getIsActive() );
        }
        if ( request.getName() != null ) {
            serviceCatalog.setName( request.getName() );
        }
    }
}
