package com.example.appointment_service.service;

import com.example.appointment_service.dto.CreateServiceTypeRequest;
import com.example.appointment_service.dto.ServiceTypeResponse;
import com.example.appointment_service.dto.UpdateServiceTypeRequest;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ServiceTypeService {
    Mono<ServiceTypeResponse> create(CreateServiceTypeRequest request);
    Mono<ServiceTypeResponse> getById(Long id);
    Mono<List<ServiceTypeResponse>> getAll();
    Mono<Void> deleteById(Long id);
    Mono<ServiceTypeResponse> update(Long id, UpdateServiceTypeRequest request);

    Mono<List<ServiceTypeResponse>> findAll();
}
