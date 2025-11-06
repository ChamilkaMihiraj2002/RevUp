package com.example.project_service.controller;

import com.example.project_service.dto.request.CreateProjectRequest;
import com.example.project_service.dto.request.UpdateProjectRequest;
import com.example.project_service.dto.ProjectDto;
import com.example.project_service.Enum.ProjectStatus;
import com.example.project_service.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

@Slf4j
@RestController
@RequestMapping("/api/v1/projects")
@Tag(name = "Project Management", description = "APIs for managing projects in the RevUp system")
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    @Operation(summary = "Create a new project", description = "Creates a new project for a specific user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Project created successfully",
                    content = @Content(schema = @Schema(implementation = ProjectDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Mono<ResponseEntity<ProjectDto>> createProject(
            @Parameter(description = "Project creation request", required = true)
            @Valid @RequestBody CreateProjectRequest request) {
        log.info("Creating new project for user: {}", request.getUserId());
        return projectService.createProject(request)
                .map(project -> new ResponseEntity<>(project, HttpStatus.CREATED))
                .doOnSuccess(project -> log.info("Project created successfully with id: {}", project.getBody().getProjectId()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get project by ID", description = "Retrieves a specific project by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Project found",
                    content = @Content(schema = @Schema(implementation = ProjectDto.class))),
            @ApiResponse(responseCode = "404", description = "Project not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Mono<ResponseEntity<ProjectDto>> getProject(
            @Parameter(description = "Project ID", required = true, example = "1")
            @PathVariable Long id) {
        log.info("Fetching project with id: {}", id);
        return projectService.getProjectById(id)
                .map(ResponseEntity::ok);
    }

    @GetMapping
    @Operation(summary = "Get all projects", description = "Retrieves all projects in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Projects retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Flux<ProjectDto> getAllProjects() {
        log.info("Fetching all projects");
        return projectService.getAllProjects();
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get projects by user ID", description = "Retrieves all projects for a specific user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Projects retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Flux<ProjectDto> getProjectsByUserId(
            @Parameter(description = "User ID", required = true, example = "1")
            @PathVariable Long userId) {
        log.info("Fetching projects for user: {}", userId);
        return projectService.getProjectsByUserId(userId);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get projects by status", 
            description = "Retrieves all projects with a specific status (PENDING, IN_PROGRESS, COMPLETED, CANCELLED, ON_HOLD)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Projects retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid status provided"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Flux<ProjectDto> getProjectsByStatus(
            @Parameter(description = "Project status", required = true, 
                    example = "PENDING", 
                    schema = @Schema(allowableValues = {"PENDING", "IN_PROGRESS", "COMPLETED", "CANCELLED", "ON_HOLD"}))
            @PathVariable String status) {
        log.info("Fetching projects with status: {}", status);
        ProjectStatus projectStatus = ProjectStatus.valueOf(status.toUpperCase());
        return projectService.getProjectsByStatus(projectStatus);
    }

    @GetMapping("/user/{userId}/status/{status}")
    @Operation(summary = "Get projects by user ID and status", 
            description = "Retrieves all projects for a specific user with a specific status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Projects retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid status provided"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Flux<ProjectDto> getProjectsByUserIdAndStatus(
            @Parameter(description = "User ID", required = true, example = "1")
            @PathVariable Long userId,
            @Parameter(description = "Project status", required = true, 
                    example = "IN_PROGRESS",
                    schema = @Schema(allowableValues = {"PENDING", "IN_PROGRESS", "COMPLETED", "CANCELLED", "ON_HOLD"}))
            @PathVariable String status) {
        log.info("Fetching projects for user: {} with status: {}", userId, status);
        ProjectStatus projectStatus = ProjectStatus.valueOf(status.toUpperCase());
        return projectService.getProjectsByUserIdAndStatus(userId, projectStatus);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a project", description = "Updates an existing project by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Project updated successfully",
                    content = @Content(schema = @Schema(implementation = ProjectDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "404", description = "Project not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Mono<ResponseEntity<ProjectDto>> updateProject(
            @Parameter(description = "Project ID", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Project update request", required = true)
            @Valid @RequestBody UpdateProjectRequest request) {
        log.info("Updating project with id: {}", id);
        return projectService.updateProject(id, request)
                .map(ResponseEntity::ok)
                .doOnSuccess(project -> log.info("Project updated successfully with id: {}", id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a project", description = "Deletes a project by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Project deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Project not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Mono<ResponseEntity<Void>> deleteProject(
            @Parameter(description = "Project ID", required = true, example = "1")
            @PathVariable Long id) {
        log.info("Deleting project with id: {}", id);
        return projectService.deleteProject(id)
                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)))
                .doOnSuccess(response -> log.info("Project deleted successfully with id: {}", id));
    }

    @GetMapping("/{projectId}/exists")
    @Operation(summary = "Check if project exists", description = "Checks whether a project with the given ID exists")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Check completed successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Mono<ResponseEntity<Boolean>> checkProjectExists(
            @Parameter(description = "Project ID", required = true, example = "1")
            @PathVariable Long projectId) {
        log.info("Checking if project exists with id: {}", projectId);
        return projectService.projectExists(projectId)
                .map(ResponseEntity::ok);
    }
}
