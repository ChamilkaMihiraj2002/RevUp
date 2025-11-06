package com.example.appointment_service.mapper;

import com.example.appointment_service.dto.CreateServiceTypeRequest;
import com.example.appointment_service.dto.ServiceTypeResponse;
import com.example.appointment_service.entity.ServiceType;

public class ServiceTypeMapper {

    public static ServiceType toEntity(CreateServiceTypeRequest dto) {
        return ServiceType.builder()
                .code(dto.getCode())
                .name(dto.getName())
                .description(dto.getDescription())
                .baseDurationMinutes(dto.getBaseDurationMinutes())
                .basePrice(dto.getBasePrice())
                .build();
    }

    public static ServiceTypeResponse toResponse(ServiceType entity) {
        return ServiceTypeResponse.builder()
                .serviceTypeId(entity.getServiceTypeId())
                .code(entity.getCode())
                .name(entity.getName())
                .description(entity.getDescription())
                .baseDurationMinutes(entity.getBaseDurationMinutes())
                .basePrice(entity.getBasePrice())
                .build();
    }
}
