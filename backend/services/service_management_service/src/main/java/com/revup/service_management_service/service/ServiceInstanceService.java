package com.revup.service_management_service.service;

import com.revup.service_management_service.dto.CreateServiceInstanceRequest;
import com.revup.service_management_service.dto.ProgressUpdateRequest;
import com.revup.service_management_service.dto.ServiceInstanceResponse;
import com.revup.service_management_service.dto.UpdateServiceInstanceRequest;
import com.revup.service_management_service.exception.ResourceNotFoundException;
import com.revup.service_management_service.mapper.ServiceInstanceMapper;
import com.revup.service_management_service.model.ServiceInstance;
import com.revup.service_management_service.model.ServiceStatus;
import com.revup.service_management_service.repository.ServiceInstanceRepository;
import com.revup.service_management_service.websocket.ServiceProgressWebSocketHandler;
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
public class ServiceInstanceService {

    private final ServiceInstanceRepository serviceInstanceRepository;
    private final ServiceInstanceMapper serviceInstanceMapper;
    private final ServiceProgressWebSocketHandler webSocketHandler;

    @Transactional
    public Mono<ServiceInstanceResponse> createServiceInstance(CreateServiceInstanceRequest request) {
        log.info("Creating service instance for appointment ID: {}", request.getAppointmentId());

        ServiceInstance serviceInstance = serviceInstanceMapper.toEntity(request);
        if (serviceInstance.getStatus() == null) {
            serviceInstance.setStatus(ServiceStatus.SCHEDULED);
        }
        if (serviceInstance.getProgressPercentage() == null) {
            serviceInstance.setProgressPercentage(0);
        }
        serviceInstance.setCreatedAt(LocalDateTime.now());
        serviceInstance.setUpdatedAt(LocalDateTime.now());

        return serviceInstanceRepository.save(serviceInstance)
                .map(serviceInstanceMapper::toResponse)
                .doOnSuccess(response -> webSocketHandler.broadcastProgress(response));
    }

    public Mono<ServiceInstanceResponse> getServiceInstanceById(Long id) {
        log.info("Fetching service instance with ID: {}", id);
        return serviceInstanceRepository.findById(id)
                .map(serviceInstanceMapper::toResponse)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Service instance not found with ID: " + id)));
    }

    public Flux<ServiceInstanceResponse> getAllServiceInstances() {
        log.info("Fetching all service instances");
        return serviceInstanceRepository.findAll()
                .map(serviceInstanceMapper::toResponse);
    }

    public Flux<ServiceInstanceResponse> getServiceInstancesByAppointment(Long appointmentId) {
        log.info("Fetching service instances for appointment ID: {}", appointmentId);
        return serviceInstanceRepository.findByAppointmentId(appointmentId)
                .map(serviceInstanceMapper::toResponse);
    }

    public Flux<ServiceInstanceResponse> getServiceInstancesByEmployee(Long employeeId) {
        log.info("Fetching service instances for employee ID: {}", employeeId);
        return serviceInstanceRepository.findByEmployeeId(employeeId)
                .map(serviceInstanceMapper::toResponse);
    }

    public Flux<ServiceInstanceResponse> getServiceInstancesByStatus(ServiceStatus status) {
        log.info("Fetching service instances with status: {}", status);
        return serviceInstanceRepository.findByStatus(status)
                .map(serviceInstanceMapper::toResponse);
    }

    public Flux<ServiceInstanceResponse> getInProgressServices() {
        log.info("Fetching in-progress service instances");
        return serviceInstanceRepository.findAllInProgress()
                .map(serviceInstanceMapper::toResponse);
    }

    public Mono<Double> getOverallProgress(Long appointmentId) {
        log.info("Calculating overall progress for appointment ID: {}", appointmentId);
        return serviceInstanceRepository.calculateOverallProgressByAppointment(appointmentId)
                .defaultIfEmpty(0.0);
    }

    @Transactional
    public Mono<ServiceInstanceResponse> updateServiceInstance(Long id, UpdateServiceInstanceRequest request) {
        log.info("Updating service instance with ID: {}", id);
        return serviceInstanceRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Service instance not found with ID: " + id)))
                .flatMap(serviceInstance -> {
                    serviceInstanceMapper.updateServiceInstanceFromRequest(request, serviceInstance);
                    serviceInstance.setUpdatedAt(LocalDateTime.now());
                    return serviceInstanceRepository.save(serviceInstance);
                })
                .map(serviceInstanceMapper::toResponse)
                .doOnSuccess(response -> webSocketHandler.broadcastProgress(response));
    }

    @Transactional
    public Mono<ServiceInstanceResponse> updateProgress(ProgressUpdateRequest request) {
        log.info("Updating progress for service instance ID: {} to {}%", 
                request.getServiceInstanceId(), request.getProgressPercentage());
        
        return serviceInstanceRepository.findById(request.getServiceInstanceId())
                .switchIfEmpty(Mono.error(new ResourceNotFoundException(
                        "Service instance not found with ID: " + request.getServiceInstanceId())))
                .flatMap(serviceInstance -> {
                    serviceInstance.setProgressPercentage(request.getProgressPercentage());
                    if (request.getNotes() != null) {
                        serviceInstance.setNotes(request.getNotes());
                    }
                    
                    // Auto-update status based on progress
                    if (request.getProgressPercentage() == 0) {
                        serviceInstance.setStatus(ServiceStatus.SCHEDULED);
                    } else if (request.getProgressPercentage() > 0 && request.getProgressPercentage() < 100) {
                        if (serviceInstance.getStatus() == ServiceStatus.SCHEDULED) {
                            serviceInstance.setStatus(ServiceStatus.IN_PROGRESS);
                            serviceInstance.setStartDate(LocalDateTime.now());
                        }
                    } else if (request.getProgressPercentage() == 100) {
                        serviceInstance.setStatus(ServiceStatus.COMPLETED);
                        serviceInstance.setEndDate(LocalDateTime.now());
                    }
                    
                    serviceInstance.setUpdatedAt(LocalDateTime.now());
                    return serviceInstanceRepository.save(serviceInstance);
                })
                .map(serviceInstanceMapper::toResponse)
                .doOnSuccess(response -> webSocketHandler.broadcastProgress(response));
    }

    @Transactional
    public Mono<ServiceInstanceResponse> startService(Long id) {
        log.info("Starting service instance ID: {}", id);
        return serviceInstanceRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Service instance not found with ID: " + id)))
                .flatMap(serviceInstance -> {
                    serviceInstance.setStatus(ServiceStatus.IN_PROGRESS);
                    serviceInstance.setStartDate(LocalDateTime.now());
                    if (serviceInstance.getProgressPercentage() == 0) {
                        serviceInstance.setProgressPercentage(1);
                    }
                    serviceInstance.setUpdatedAt(LocalDateTime.now());
                    return serviceInstanceRepository.save(serviceInstance);
                })
                .map(serviceInstanceMapper::toResponse)
                .doOnSuccess(response -> webSocketHandler.broadcastProgress(response));
    }

    @Transactional
    public Mono<ServiceInstanceResponse> completeService(Long id) {
        log.info("Completing service instance ID: {}", id);
        return serviceInstanceRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Service instance not found with ID: " + id)))
                .flatMap(serviceInstance -> {
                    serviceInstance.setStatus(ServiceStatus.COMPLETED);
                    serviceInstance.setProgressPercentage(100);
                    serviceInstance.setEndDate(LocalDateTime.now());
                    serviceInstance.setUpdatedAt(LocalDateTime.now());
                    return serviceInstanceRepository.save(serviceInstance);
                })
                .map(serviceInstanceMapper::toResponse)
                .doOnSuccess(response -> webSocketHandler.broadcastProgress(response));
    }

    @Transactional
    public Mono<Void> deleteServiceInstance(Long id) {
        log.info("Deleting service instance with ID: {}", id);
        return serviceInstanceRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Service instance not found with ID: " + id)))
                .flatMap(serviceInstanceRepository::delete);
    }

    public Mono<Long> getCompletedServicesCount(Long employeeId) {
        log.info("Fetching completed services count for employee ID: {}", employeeId);
        return serviceInstanceRepository.countCompletedServicesByEmployee(employeeId);
    }
}
