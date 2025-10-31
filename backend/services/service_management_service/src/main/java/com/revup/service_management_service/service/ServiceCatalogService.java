package com.revup.service_management_service.service;

import com.revup.service_management_service.dto.CreateServiceCatalogRequest;
import com.revup.service_management_service.dto.ServiceCatalogResponse;
import com.revup.service_management_service.exception.DuplicateResourceException;
import com.revup.service_management_service.exception.ResourceNotFoundException;
import com.revup.service_management_service.mapper.ServiceCatalogMapper;
import com.revup.service_management_service.model.ServiceCatalog;
import com.revup.service_management_service.repository.ServiceCatalogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class ServiceCatalogService {

    private final ServiceCatalogRepository serviceCatalogRepository;
    private final ServiceCatalogMapper serviceCatalogMapper;

    @Transactional
    public Mono<ServiceCatalogResponse> createServiceCatalog(CreateServiceCatalogRequest request) {
        log.info("Creating service catalog: {}", request.getName());

        return serviceCatalogRepository.existsByName(request.getName())
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new DuplicateResourceException(
                                "Service with name already exists: " + request.getName()));
                    }
                    ServiceCatalog serviceCatalog = serviceCatalogMapper.toEntity(request);
                    if (serviceCatalog.getIsActive() == null) {
                        serviceCatalog.setIsActive(true);
                    }
                    serviceCatalog.setCreatedAt(LocalDateTime.now());
                    serviceCatalog.setUpdatedAt(LocalDateTime.now());
                    return serviceCatalogRepository.save(serviceCatalog);
                })
                .map(serviceCatalogMapper::toResponse);
    }

    public Mono<ServiceCatalogResponse> getServiceCatalogById(Long id) {
        log.info("Fetching service catalog with ID: {}", id);
        return serviceCatalogRepository.findById(id)
                .map(serviceCatalogMapper::toResponse)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Service catalog not found with ID: " + id)));
    }

    public Flux<ServiceCatalogResponse> getAllServiceCatalogs() {
        log.info("Fetching all service catalogs");
        return serviceCatalogRepository.findAll()
                .map(serviceCatalogMapper::toResponse);
    }

    public Flux<ServiceCatalogResponse> getActiveServiceCatalogs() {
        log.info("Fetching active service catalogs");
        return serviceCatalogRepository.findAllActiveServices()
                .map(serviceCatalogMapper::toResponse);
    }

    public Flux<ServiceCatalogResponse> getServiceCatalogsByCategory(String category) {
        log.info("Fetching service catalogs by category: {}", category);
        return serviceCatalogRepository.findActiveByCategoryOrderByPrice(category)
                .map(serviceCatalogMapper::toResponse);
    }

    public Flux<ServiceCatalogResponse> searchServiceCatalogsByName(String name) {
        log.info("Searching service catalogs by name: {}", name);
        return serviceCatalogRepository.findByNameContainingIgnoreCase(name)
                .map(serviceCatalogMapper::toResponse);
    }

    @Transactional
    public Mono<ServiceCatalogResponse> updateServiceCatalog(Long id, CreateServiceCatalogRequest request) {
        log.info("Updating service catalog with ID: {}", id);
        return serviceCatalogRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Service catalog not found with ID: " + id)))
                .flatMap(serviceCatalog -> {
                    serviceCatalogMapper.updateServiceCatalogFromRequest(request, serviceCatalog);
                    serviceCatalog.setUpdatedAt(LocalDateTime.now());
                    return serviceCatalogRepository.save(serviceCatalog);
                })
                .map(serviceCatalogMapper::toResponse);
    }

    @Transactional
    public Mono<Void> deleteServiceCatalog(Long id) {
        log.info("Deleting service catalog with ID: {}", id);
        return serviceCatalogRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Service catalog not found with ID: " + id)))
                .flatMap(serviceCatalogRepository::delete);
    }

    @Transactional
    public Mono<ServiceCatalogResponse> toggleActiveStatus(Long id) {
        log.info("Toggling active status for service catalog ID: {}", id);
        return serviceCatalogRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Service catalog not found with ID: " + id)))
                .flatMap(serviceCatalog -> {
                    serviceCatalog.setIsActive(!serviceCatalog.getIsActive());
                    serviceCatalog.setUpdatedAt(LocalDateTime.now());
                    return serviceCatalogRepository.save(serviceCatalog);
                })
                .map(serviceCatalogMapper::toResponse);
    }
}
