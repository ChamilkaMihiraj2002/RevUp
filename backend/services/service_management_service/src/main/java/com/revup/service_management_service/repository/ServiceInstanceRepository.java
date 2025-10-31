package com.revup.service_management_service.repository;

import com.revup.service_management_service.model.ServiceInstance;
import com.revup.service_management_service.model.ServiceStatus;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ServiceInstanceRepository extends R2dbcRepository<ServiceInstance, Long> {

    Flux<ServiceInstance> findByAppointmentId(Long appointmentId);

    Flux<ServiceInstance> findByServiceCatalogId(Long serviceCatalogId);

    Flux<ServiceInstance> findByEmployeeId(Long employeeId);

    Flux<ServiceInstance> findByStatus(ServiceStatus status);

    @Query("SELECT * FROM service_schema.service_instances " +
           "WHERE employee_id = :employeeId AND status = :status " +
           "ORDER BY start_date DESC")
    Flux<ServiceInstance> findByEmployeeIdAndStatus(Long employeeId, ServiceStatus status);

    @Query("SELECT * FROM service_schema.service_instances " +
           "WHERE status = 'IN_PROGRESS' " +
           "ORDER BY progress_percentage ASC, start_date")
    Flux<ServiceInstance> findAllInProgress();

    @Query("SELECT AVG(progress_percentage) FROM service_schema.service_instances " +
           "WHERE appointment_id = :appointmentId AND status != 'CANCELLED'")
    Mono<Double> calculateOverallProgressByAppointment(Long appointmentId);

    @Query("SELECT COUNT(*) FROM service_schema.service_instances " +
           "WHERE employee_id = :employeeId AND status = 'COMPLETED'")
    Mono<Long> countCompletedServicesByEmployee(Long employeeId);
}
