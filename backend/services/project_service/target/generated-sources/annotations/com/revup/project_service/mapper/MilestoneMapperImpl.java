package com.revup.project_service.mapper;

import com.revup.project_service.dto.CreateMilestoneRequest;
import com.revup.project_service.dto.MilestoneResponse;
import com.revup.project_service.dto.UpdateMilestoneRequest;
import com.revup.project_service.entity.ProjectMilestone;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-10-31T12:54:58+0530",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.44.0.v20251001-1143, environment: Java 21.0.8 (Eclipse Adoptium)"
)
@Component
public class MilestoneMapperImpl implements MilestoneMapper {

    @Override
    public ProjectMilestone toEntity(CreateMilestoneRequest request) {
        if ( request == null ) {
            return null;
        }

        ProjectMilestone projectMilestone = new ProjectMilestone();

        projectMilestone.setDescription( request.getDescription() );
        projectMilestone.setDueDate( request.getDueDate() );
        projectMilestone.setProjectId( request.getProjectId() );
        projectMilestone.setTitle( request.getTitle() );

        return projectMilestone;
    }

    @Override
    public MilestoneResponse toResponse(ProjectMilestone milestone) {
        if ( milestone == null ) {
            return null;
        }

        MilestoneResponse milestoneResponse = new MilestoneResponse();

        milestoneResponse.setCompletedDate( milestone.getCompletedDate() );
        milestoneResponse.setCreatedAt( milestone.getCreatedAt() );
        milestoneResponse.setDescription( milestone.getDescription() );
        milestoneResponse.setDueDate( milestone.getDueDate() );
        milestoneResponse.setId( milestone.getId() );
        milestoneResponse.setProjectId( milestone.getProjectId() );
        milestoneResponse.setStatus( milestone.getStatus() );
        milestoneResponse.setTitle( milestone.getTitle() );
        milestoneResponse.setUpdatedAt( milestone.getUpdatedAt() );

        return milestoneResponse;
    }

    @Override
    public void updateEntityFromRequest(UpdateMilestoneRequest request, ProjectMilestone milestone) {
        if ( request == null ) {
            return;
        }

        if ( request.getCompletedDate() != null ) {
            milestone.setCompletedDate( request.getCompletedDate() );
        }
        if ( request.getDescription() != null ) {
            milestone.setDescription( request.getDescription() );
        }
        if ( request.getDueDate() != null ) {
            milestone.setDueDate( request.getDueDate() );
        }
        if ( request.getStatus() != null ) {
            milestone.setStatus( request.getStatus() );
        }
        if ( request.getTitle() != null ) {
            milestone.setTitle( request.getTitle() );
        }
    }
}
