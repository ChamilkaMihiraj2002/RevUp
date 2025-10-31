package com.revup.project_service.repository;

import com.revup.project_service.entity.Project;
import com.revup.project_service.enums.ProjectStatus;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ProjectRepository extends R2dbcRepository<Project, Long> {
    
    Mono<Project> findByProjectCode(String projectCode);
    
    Mono<Boolean> existsByProjectCode(String projectCode);
    
    Flux<Project> findByCustomerId(Long customerId);
    
    Flux<Project> findByVehicleId(Long vehicleId);
    
    Flux<Project> findByStatus(ProjectStatus status);
    
    @Query("SELECT * FROM project_schema.projects WHERE customer_id = :customerId AND status = :status")
    Flux<Project> findByCustomerIdAndStatus(Long customerId, ProjectStatus status);
    
    @Query("SELECT * FROM project_schema.projects WHERE status IN ('IN_PROGRESS', 'APPROVED') ORDER BY created_at DESC")
    Flux<Project> findAllActive();
    
    @Query("SELECT * FROM project_schema.projects WHERE title ILIKE :searchTerm OR description ILIKE :searchTerm")
    Flux<Project> searchProjects(String searchTerm);
}
