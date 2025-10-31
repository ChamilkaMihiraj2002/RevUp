package com.revup.project_service.service;

import com.revup.project_service.dto.CreateProjectRequest;
import com.revup.project_service.dto.ProjectResponse;
import com.revup.project_service.dto.UpdateProjectRequest;
import com.revup.project_service.entity.Project;
import com.revup.project_service.enums.ProjectStatus;
import com.revup.project_service.exception.DuplicateResourceException;
import com.revup.project_service.exception.ResourceNotFoundException;
import com.revup.project_service.mapper.ProjectMapper;
import com.revup.project_service.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectService {
    
    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final Random random = new Random();
    
    public Mono<ProjectResponse> createProject(CreateProjectRequest request) {
        log.info("Creating new project for customer: {}", request.getCustomerId());
        
        return generateUniqueProjectCode()
                .flatMap(code -> {
                    Project project = projectMapper.toEntity(request);
                    project.setProjectCode(code);
                    project.setStatus(ProjectStatus.DRAFT);
                    project.setActualCost(BigDecimal.ZERO);
                    project.setCreatedAt(LocalDateTime.now());
                    project.setUpdatedAt(LocalDateTime.now());
                    
                    return projectRepository.save(project);
                })
                .map(projectMapper::toResponse)
                .doOnSuccess(response -> log.info("Project created with code: {}", response.getProjectCode()))
                .doOnError(e -> log.error("Error creating project", e));
    }
    
    public Mono<ProjectResponse> getProjectById(Long id) {
        return projectRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Project not found with id: " + id)))
                .map(projectMapper::toResponse);
    }
    
    public Mono<ProjectResponse> getProjectByCode(String code) {
        return projectRepository.findByProjectCode(code)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Project not found with code: " + code)))
                .map(projectMapper::toResponse);
    }
    
    public Flux<ProjectResponse> getAllProjects() {
        return projectRepository.findAll()
                .map(projectMapper::toResponse);
    }
    
    public Flux<ProjectResponse> getProjectsByCustomer(Long customerId) {
        return projectRepository.findByCustomerId(customerId)
                .map(projectMapper::toResponse);
    }
    
    public Flux<ProjectResponse> getProjectsByVehicle(Long vehicleId) {
        return projectRepository.findByVehicleId(vehicleId)
                .map(projectMapper::toResponse);
    }
    
    public Flux<ProjectResponse> getProjectsByStatus(ProjectStatus status) {
        return projectRepository.findByStatus(status)
                .map(projectMapper::toResponse);
    }
    
    public Flux<ProjectResponse> getActiveProjects() {
        return projectRepository.findAllActive()
                .map(projectMapper::toResponse);
    }
    
    public Flux<ProjectResponse> searchProjects(String searchTerm) {
        String pattern = "%" + searchTerm + "%";
        return projectRepository.searchProjects(pattern)
                .map(projectMapper::toResponse);
    }
    
    public Mono<ProjectResponse> updateProject(Long id, UpdateProjectRequest request) {
        log.info("Updating project with id: {}", id);
        
        return projectRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Project not found with id: " + id)))
                .flatMap(existingProject -> {
                    projectMapper.updateEntityFromRequest(request, existingProject);
                    return projectRepository.save(existingProject);
                })
                .map(projectMapper::toResponse)
                .doOnSuccess(response -> log.info("Project updated: {}", response.getProjectCode()));
    }
    
    public Mono<ProjectResponse> approveProject(Long id) {
        return updateProjectStatus(id, ProjectStatus.APPROVED);
    }
    
    public Mono<ProjectResponse> startProject(Long id) {
        return projectRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Project not found with id: " + id)))
                .flatMap(project -> {
                    project.setStatus(ProjectStatus.IN_PROGRESS);
                    if (project.getStartDate() == null) {
                        project.setStartDate(LocalDateTime.now());
                    }
                    return projectRepository.save(project);
                })
                .map(projectMapper::toResponse);
    }
    
    public Mono<ProjectResponse> completeProject(Long id) {
        return projectRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Project not found with id: " + id)))
                .flatMap(project -> {
                    project.setStatus(ProjectStatus.COMPLETED);
                    project.setEndDate(LocalDateTime.now());
                    return projectRepository.save(project);
                })
                .map(projectMapper::toResponse);
    }
    
    public Mono<ProjectResponse> cancelProject(Long id) {
        return updateProjectStatus(id, ProjectStatus.CANCELLED);
    }
    
    public Mono<Void> deleteProject(Long id) {
        return projectRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Project not found with id: " + id)))
                .flatMap(project -> projectRepository.deleteById(id));
    }
    
    private Mono<ProjectResponse> updateProjectStatus(Long id, ProjectStatus status) {
        return projectRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Project not found with id: " + id)))
                .flatMap(project -> {
                    project.setStatus(status);
                    return projectRepository.save(project);
                })
                .map(projectMapper::toResponse);
    }
    
    private Mono<String> generateUniqueProjectCode() {
        return Mono.defer(() -> {
            String code = "PROJ-" + String.format("%08d", random.nextInt(100000000));
            
            return projectRepository.existsByProjectCode(code)
                    .flatMap(exists -> exists ? generateUniqueProjectCode() : Mono.just(code));
        });
    }
}
