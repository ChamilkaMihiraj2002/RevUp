package com.revup.service_management_service.mapper;

import com.revup.service_management_service.dto.CreateServiceCatalogRequest;
import com.revup.service_management_service.dto.ServiceCatalogResponse;
import com.revup.service_management_service.model.ServiceCatalog;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ServiceCatalogMapper {

    ServiceCatalogResponse toResponse(ServiceCatalog serviceCatalog);

    ServiceCatalog toEntity(CreateServiceCatalogRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateServiceCatalogFromRequest(CreateServiceCatalogRequest request, @MappingTarget ServiceCatalog serviceCatalog);
}
