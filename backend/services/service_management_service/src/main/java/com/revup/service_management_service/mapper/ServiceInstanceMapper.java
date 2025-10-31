package com.revup.service_management_service.mapper;

import com.revup.service_management_service.dto.CreateServiceInstanceRequest;
import com.revup.service_management_service.dto.ServiceInstanceResponse;
import com.revup.service_management_service.dto.UpdateServiceInstanceRequest;
import com.revup.service_management_service.model.ServiceInstance;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ServiceInstanceMapper {

    ServiceInstanceResponse toResponse(ServiceInstance serviceInstance);

    ServiceInstance toEntity(CreateServiceInstanceRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateServiceInstanceFromRequest(UpdateServiceInstanceRequest request, @MappingTarget ServiceInstance serviceInstance);
}
