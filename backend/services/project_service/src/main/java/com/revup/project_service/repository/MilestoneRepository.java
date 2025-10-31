package com.revup.project_service.repository;

import com.revup.project_service.entity.ProjectMilestone;
import com.revup.project_service.enums.MilestoneStatus;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Repository
public interface MilestoneRepository extends R2dbcRepository<ProjectMilestone, Long> {
    
    Flux<ProjectMilestone> findByProjectId(Long projectId);
    
    Flux<ProjectMilestone> findByStatus(MilestoneStatus status);
    
    @Query("SELECT * FROM project_schema.project_milestones WHERE project_id = :projectId AND status = :status")
    Flux<ProjectMilestone> findByProjectIdAndStatus(Long projectId, MilestoneStatus status);
    
    @Query("SELECT * FROM project_schema.project_milestones WHERE due_date < :date AND status != 'COMPLETED'")
    Flux<ProjectMilestone> findOverdueMilestones(LocalDateTime date);
    
    @Query("SELECT COUNT(*) FROM project_schema.project_milestones WHERE project_id = :projectId AND status = 'COMPLETED'")
    Mono<Long> countCompletedByProjectId(Long projectId);
    
    @Query("SELECT COUNT(*) FROM project_schema.project_milestones WHERE project_id = :projectId")
    Mono<Long> countByProjectId(Long projectId);
}
