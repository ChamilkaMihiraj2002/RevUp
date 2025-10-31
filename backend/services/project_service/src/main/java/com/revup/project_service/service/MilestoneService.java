package com.revup.project_service.service;

import com.revup.project_service.dto.CreateMilestoneRequest;
import com.revup.project_service.dto.MilestoneResponse;
import com.revup.project_service.dto.UpdateMilestoneRequest;
import com.revup.project_service.entity.ProjectMilestone;
import com.revup.project_service.enums.MilestoneStatus;
import com.revup.project_service.exception.ResourceNotFoundException;
import com.revup.project_service.mapper.MilestoneMapper;
import com.revup.project_service.repository.MilestoneRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class MilestoneService {
    
    private final MilestoneRepository milestoneRepository;
    private final MilestoneMapper milestoneMapper;
    
    public Mono<MilestoneResponse> createMilestone(CreateMilestoneRequest request) {
        log.info("Creating milestone for project: {}", request.getProjectId());
        
        ProjectMilestone milestone = milestoneMapper.toEntity(request);
        milestone.setStatus(MilestoneStatus.PENDING);
        milestone.setCreatedAt(LocalDateTime.now());
        milestone.setUpdatedAt(LocalDateTime.now());
        
        return milestoneRepository.save(milestone)
                .map(milestoneMapper::toResponse)
                .doOnSuccess(response -> log.info("Milestone created with id: {}", response.getId()));
    }
    
    public Mono<MilestoneResponse> getMilestoneById(Long id) {
        return milestoneRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Milestone not found with id: " + id)))
                .map(milestoneMapper::toResponse);
    }
    
    public Flux<MilestoneResponse> getMilestonesByProject(Long projectId) {
        return milestoneRepository.findByProjectId(projectId)
                .map(milestoneMapper::toResponse);
    }
    
    public Flux<MilestoneResponse> getMilestonesByStatus(MilestoneStatus status) {
        return milestoneRepository.findByStatus(status)
                .map(milestoneMapper::toResponse);
    }
    
    public Flux<MilestoneResponse> getOverdueMilestones() {
        return milestoneRepository.findOverdueMilestones(LocalDateTime.now())
                .map(milestoneMapper::toResponse);
    }
    
    public Mono<Double> getProjectCompletionPercentage(Long projectId) {
        return Mono.zip(
                milestoneRepository.countCompletedByProjectId(projectId),
                milestoneRepository.countByProjectId(projectId)
        ).map(tuple -> {
            Long completed = tuple.getT1();
            Long total = tuple.getT2();
            if (total == 0) return 0.0;
            return (completed.doubleValue() / total.doubleValue()) * 100;
        });
    }
    
    public Mono<MilestoneResponse> updateMilestone(Long id, UpdateMilestoneRequest request) {
        log.info("Updating milestone with id: {}", id);
        
        return milestoneRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Milestone not found with id: " + id)))
                .flatMap(existingMilestone -> {
                    milestoneMapper.updateEntityFromRequest(request, existingMilestone);
                    
                    // Auto-set completed date when status changes to COMPLETED
                    if (request.getStatus() == MilestoneStatus.COMPLETED && existingMilestone.getCompletedDate() == null) {
                        existingMilestone.setCompletedDate(LocalDateTime.now());
                    }
                    
                    return milestoneRepository.save(existingMilestone);
                })
                .map(milestoneMapper::toResponse)
                .doOnSuccess(response -> log.info("Milestone updated: {}", response.getId()));
    }
    
    public Mono<MilestoneResponse> startMilestone(Long id) {
        return updateMilestoneStatus(id, MilestoneStatus.IN_PROGRESS);
    }
    
    public Mono<MilestoneResponse> completeMilestone(Long id) {
        return milestoneRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Milestone not found with id: " + id)))
                .flatMap(milestone -> {
                    milestone.setStatus(MilestoneStatus.COMPLETED);
                    milestone.setCompletedDate(LocalDateTime.now());
                    return milestoneRepository.save(milestone);
                })
                .map(milestoneMapper::toResponse);
    }
    
    public Mono<Void> deleteMilestone(Long id) {
        return milestoneRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Milestone not found with id: " + id)))
                .flatMap(milestone -> milestoneRepository.deleteById(id));
    }
    
    private Mono<MilestoneResponse> updateMilestoneStatus(Long id, MilestoneStatus status) {
        return milestoneRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Milestone not found with id: " + id)))
                .flatMap(milestone -> {
                    milestone.setStatus(status);
                    return milestoneRepository.save(milestone);
                })
                .map(milestoneMapper::toResponse);
    }
}
