package com.example.project_service.service.Impl;

import com.example.project_service.dto.request.CreateProjectRequest;
import com.example.project_service.dto.request.UpdateProjectRequest;
import com.example.project_service.dto.ProjectDto;
import com.example.project_service.entity.Project;
import com.example.project_service.Enum.ProjectStatus;
import com.example.project_service.exception.ResourceNotFoundException;
import com.example.project_service.mapper.ProjectMapper;
import com.example.project_service.repository.ProjectRepository;
import com.example.project_service.service.ProjectService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

@Service
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final Scheduler jdbcScheduler;
    private final ProjectMapper projectMapper;

    public ProjectServiceImpl(ProjectRepository projectRepository, 
                              Scheduler jdbcScheduler, 
                              ProjectMapper projectMapper) {
        this.projectRepository = projectRepository;
        this.jdbcScheduler = jdbcScheduler;
        this.projectMapper = projectMapper;
    }

    @Override
    public Mono<ProjectDto> createProject(CreateProjectRequest request) {
        return Mono.fromCallable(() -> {
                    Project project = projectMapper.toEntity(request);
                    Project savedProject = projectRepository.save(project);
                    return projectMapper.toDto(savedProject);
                })
                .subscribeOn(jdbcScheduler);
    }

    @Override
    public Mono<ProjectDto> getProjectById(Long id) {
        return Mono.fromCallable(() -> projectRepository.findById(id))
                .subscribeOn(jdbcScheduler)
                .flatMap(optionalProject -> optionalProject
                        .map(project -> Mono.just(projectMapper.toDto(project)))
                        .orElseGet(() -> Mono.error(new ResourceNotFoundException("Project not found with id: " + id))));
    }

    @Override
    public Flux<ProjectDto> getAllProjects() {
        return Mono.fromCallable(() -> projectRepository.findAll())
                .subscribeOn(jdbcScheduler)
                .flatMapMany(Flux::fromIterable)
                .map(projectMapper::toDto);
    }

    @Override
    public Flux<ProjectDto> getProjectsByUserId(Long userId) {
        return Mono.fromCallable(() -> projectRepository.findByUserId(userId))
                .subscribeOn(jdbcScheduler)
                .flatMapMany(Flux::fromIterable)
                .map(projectMapper::toDto);
    }

    @Override
    public Flux<ProjectDto> getProjectsByStatus(ProjectStatus status) {
        return Mono.fromCallable(() -> projectRepository.findByStatus(status))
                .subscribeOn(jdbcScheduler)
                .flatMapMany(Flux::fromIterable)
                .map(projectMapper::toDto);
    }

    @Override
    public Flux<ProjectDto> getProjectsByUserIdAndStatus(Long userId, ProjectStatus status) {
        return Mono.fromCallable(() -> projectRepository.findByUserIdAndStatus(userId, status))
                .subscribeOn(jdbcScheduler)
                .flatMapMany(Flux::fromIterable)
                .map(projectMapper::toDto);
    }

    @Override
    public Mono<ProjectDto> updateProject(Long id, UpdateProjectRequest request) {
        return Mono.fromCallable(() -> {
                    Project project = projectRepository.findById(id)
                            .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));
                    projectMapper.updateEntityFromDto(project, request);
                    Project updatedProject = projectRepository.save(project);
                    return projectMapper.toDto(updatedProject);
                })
                .subscribeOn(jdbcScheduler);
    }

    @Override
    public Mono<Void> deleteProject(Long id) {
        return Mono.fromRunnable(() -> {
                    if (!projectRepository.existsById(id)) {
                        throw new ResourceNotFoundException("Project not found with id: " + id);
                    }
                    projectRepository.deleteById(id);
                })
                .subscribeOn(jdbcScheduler)
                .then();
    }

    @Override
    public Mono<Boolean> projectExists(Long projectId) {
        return Mono.fromCallable(() -> projectRepository.existsById(projectId))
                .subscribeOn(jdbcScheduler);
    }
}
