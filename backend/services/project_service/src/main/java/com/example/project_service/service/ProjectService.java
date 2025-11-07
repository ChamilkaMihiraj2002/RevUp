package com.example.project_service.service;

import com.example.project_service.dto.request.CreateProjectRequest;
import com.example.project_service.dto.request.UpdateProjectRequest;
import com.example.project_service.dto.request.AcceptProjectRequest;
import com.example.project_service.dto.ProjectDto;
import com.example.project_service.Enum.ProjectStatus;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

public interface ProjectService {
    Mono<ProjectDto> createProject(CreateProjectRequest request);
    Mono<ProjectDto> getProjectById(Long id);
    Flux<ProjectDto> getAllProjects();
    Flux<ProjectDto> getProjectsByUserId(Long userId);
    Flux<ProjectDto> getProjectsByStatus(ProjectStatus status);
    Flux<ProjectDto> getProjectsByUserIdAndStatus(Long userId, ProjectStatus status);
    Flux<ProjectDto> getProjectsByTechnicianId(Long technicianId);
    Flux<ProjectDto> getProjectsByTechnicianIdAndStatus(Long technicianId, ProjectStatus status);
    Flux<ProjectDto> getPendingUnassignedProjects();
    Mono<ProjectDto> acceptProject(Long projectId, AcceptProjectRequest request);
    Mono<ProjectDto> updateProject(Long id, UpdateProjectRequest request);
    Mono<Void> deleteProject(Long id);
    Mono<Boolean> projectExists(Long projectId);
}
