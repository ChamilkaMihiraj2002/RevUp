package com.revup.project_service.controller;

import com.revup.project_service.dto.CreateMilestoneRequest;
import com.revup.project_service.dto.MilestoneResponse;
import com.revup.project_service.dto.UpdateMilestoneRequest;
import com.revup.project_service.enums.MilestoneStatus;
import com.revup.project_service.service.MilestoneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/milestones")
@RequiredArgsConstructor
@Tag(name = "Milestone Management", description = "APIs for project milestone management")
public class MilestoneController {
    
    private final MilestoneService milestoneService;
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create new milestone")
    public Mono<MilestoneResponse> createMilestone(@Valid @RequestBody CreateMilestoneRequest request) {
        return milestoneService.createMilestone(request);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get milestone by ID")
    public Mono<MilestoneResponse> getMilestoneById(@PathVariable Long id) {
        return milestoneService.getMilestoneById(id);
    }
    
    @GetMapping("/project/{projectId}")
    @Operation(summary = "Get milestones by project")
    public Flux<MilestoneResponse> getMilestonesByProject(@PathVariable Long projectId) {
        return milestoneService.getMilestonesByProject(projectId);
    }
    
    @GetMapping("/project/{projectId}/completion")
    @Operation(summary = "Get project completion percentage")
    public Mono<Double> getProjectCompletion(@PathVariable Long projectId) {
        return milestoneService.getProjectCompletionPercentage(projectId);
    }
    
    @GetMapping("/status/{status}")
    @Operation(summary = "Get milestones by status")
    public Flux<MilestoneResponse> getMilestonesByStatus(@PathVariable MilestoneStatus status) {
        return milestoneService.getMilestonesByStatus(status);
    }
    
    @GetMapping("/overdue")
    @Operation(summary = "Get overdue milestones")
    public Flux<MilestoneResponse> getOverdueMilestones() {
        return milestoneService.getOverdueMilestones();
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update milestone")
    public Mono<MilestoneResponse> updateMilestone(@PathVariable Long id, @Valid @RequestBody UpdateMilestoneRequest request) {
        return milestoneService.updateMilestone(id, request);
    }
    
    @PatchMapping("/{id}/start")
    @Operation(summary = "Start milestone")
    public Mono<MilestoneResponse> startMilestone(@PathVariable Long id) {
        return milestoneService.startMilestone(id);
    }
    
    @PatchMapping("/{id}/complete")
    @Operation(summary = "Complete milestone")
    public Mono<MilestoneResponse> completeMilestone(@PathVariable Long id) {
        return milestoneService.completeMilestone(id);
    }
    
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete milestone")
    public Mono<Void> deleteMilestone(@PathVariable Long id) {
        return milestoneService.deleteMilestone(id);
    }
}
