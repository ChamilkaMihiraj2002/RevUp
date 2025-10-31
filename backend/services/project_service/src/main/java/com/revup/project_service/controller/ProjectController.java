package com.revup.project_service.controller;

import com.revup.project_service.dto.CreateProjectRequest;
import com.revup.project_service.dto.ProjectResponse;
import com.revup.project_service.dto.UpdateProjectRequest;
import com.revup.project_service.enums.ProjectStatus;
import com.revup.project_service.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
@Tag(name = "Project Management", description = "APIs for project and modification request management")
public class ProjectController {
    
    private final ProjectService projectService;
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create new project")
    public Mono<ProjectResponse> createProject(@Valid @RequestBody CreateProjectRequest request) {
        return projectService.createProject(request);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get project by ID")
    public Mono<ProjectResponse> getProjectById(@PathVariable Long id) {
        return projectService.getProjectById(id);
    }
    
    @GetMapping("/code/{code}")
    @Operation(summary = "Get project by code")
    public Mono<ProjectResponse> getProjectByCode(@PathVariable String code) {
        return projectService.getProjectByCode(code);
    }
    
    @GetMapping
    @Operation(summary = "Get all projects")
    public Flux<ProjectResponse> getAllProjects() {
        return projectService.getAllProjects();
    }
    
    @GetMapping("/customer/{customerId}")
    @Operation(summary = "Get projects by customer")
    public Flux<ProjectResponse> getProjectsByCustomer(@PathVariable Long customerId) {
        return projectService.getProjectsByCustomer(customerId);
    }
    
    @GetMapping("/vehicle/{vehicleId}")
    @Operation(summary = "Get projects by vehicle")
    public Flux<ProjectResponse> getProjectsByVehicle(@PathVariable Long vehicleId) {
        return projectService.getProjectsByVehicle(vehicleId);
    }
    
    @GetMapping("/status/{status}")
    @Operation(summary = "Get projects by status")
    public Flux<ProjectResponse> getProjectsByStatus(@PathVariable ProjectStatus status) {
        return projectService.getProjectsByStatus(status);
    }
    
    @GetMapping("/active")
    @Operation(summary = "Get active projects")
    public Flux<ProjectResponse> getActiveProjects() {
        return projectService.getActiveProjects();
    }
    
    @GetMapping("/search")
    @Operation(summary = "Search projects")
    public Flux<ProjectResponse> searchProjects(@RequestParam String term) {
        return projectService.searchProjects(term);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update project")
    public Mono<ProjectResponse> updateProject(@PathVariable Long id, @Valid @RequestBody UpdateProjectRequest request) {
        return projectService.updateProject(id, request);
    }
    
    @PatchMapping("/{id}/approve")
    @Operation(summary = "Approve project")
    public Mono<ProjectResponse> approveProject(@PathVariable Long id) {
        return projectService.approveProject(id);
    }
    
    @PatchMapping("/{id}/start")
    @Operation(summary = "Start project")
    public Mono<ProjectResponse> startProject(@PathVariable Long id) {
        return projectService.startProject(id);
    }
    
    @PatchMapping("/{id}/complete")
    @Operation(summary = "Complete project")
    public Mono<ProjectResponse> completeProject(@PathVariable Long id) {
        return projectService.completeProject(id);
    }
    
    @PatchMapping("/{id}/cancel")
    @Operation(summary = "Cancel project")
    public Mono<ProjectResponse> cancelProject(@PathVariable Long id) {
        return projectService.cancelProject(id);
    }
    
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete project")
    public Mono<Void> deleteProject(@PathVariable Long id) {
        return projectService.deleteProject(id);
    }
}
