package com.example.appointment_service.service.impl;

import com.example.appointment_service.dto.CreateServiceTypeRequest;
import com.example.appointment_service.dto.ServiceTypeResponse;
import com.example.appointment_service.dto.UpdateServiceTypeRequest;
import com.example.appointment_service.entity.ServiceType;
import com.example.appointment_service.exception.ResourceNotFoundException;
import com.example.appointment_service.mapper.ServiceTypeMapper;
import com.example.appointment_service.repository.ServiceTypeRepository;
import com.example.appointment_service.service.ServiceTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServiceTypeServiceImpl implements ServiceTypeService {

    private final ServiceTypeRepository serviceTypeRepository;

    @Override
    public Mono<ServiceTypeResponse> create(CreateServiceTypeRequest request) {
        if (serviceTypeRepository.existsByCode(request.getCode())) {
            return Mono.error(new IllegalArgumentException("Service type code already exists"));
        }

        ServiceType entity = ServiceTypeMapper.toEntity(request);
        return Mono.just(serviceTypeRepository.save(entity))
                .map(ServiceTypeMapper::toResponse);
    }

    @Override
    public Mono<ServiceTypeResponse> getById(Long id) {
        return Mono.justOrEmpty(serviceTypeRepository.findById(id))
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Service type not found with id: " + id)))
                .map(ServiceTypeMapper::toResponse);
    }

    @Override
    public Mono<List<ServiceTypeResponse>> getAll() {
        List<ServiceTypeResponse> list = serviceTypeRepository.findAll()
                .stream()
                .map(ServiceTypeMapper::toResponse)
                .collect(Collectors.toList());
        return Mono.just(list);
    }

    @Override
    public Mono<Void> deleteById(Long id) {
        if (!serviceTypeRepository.existsById(id)) {
            return Mono.error(new ResourceNotFoundException("Service type not found with id: " + id));
        }
        serviceTypeRepository.deleteById(id);
        return Mono.empty();
    }

    @Override
    public Mono<ServiceTypeResponse> update(Long id, UpdateServiceTypeRequest request) {
        return Mono.justOrEmpty(serviceTypeRepository.findById(id))
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Service type not found with id: " + id)))
                .map(existing -> {
                    existing.setName(request.getName());
                    existing.setDescription(request.getDescription());
                    existing.setBaseDurationMinutes(request.getBaseDurationMinutes());
                    existing.setBasePrice(request.getBasePrice());
                    return serviceTypeRepository.save(existing);
                })
                .map(ServiceTypeMapper::toResponse);
    }
}
