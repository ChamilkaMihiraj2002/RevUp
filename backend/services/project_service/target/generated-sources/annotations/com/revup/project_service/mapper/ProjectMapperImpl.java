package com.revup.project_service.mapper;

import com.revup.project_service.dto.CreateProjectRequest;
import com.revup.project_service.dto.ProjectResponse;
import com.revup.project_service.dto.UpdateProjectRequest;
import com.revup.project_service.entity.Project;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-10-31T12:54:59+0530",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.44.0.v20251001-1143, environment: Java 21.0.8 (Eclipse Adoptium)"
)
@Component
public class ProjectMapperImpl implements ProjectMapper {

    @Override
    public Project toEntity(CreateProjectRequest request) {
        if ( request == null ) {
            return null;
        }

        Project project = new Project();

        project.setCustomerId( request.getCustomerId() );
        project.setDescription( request.getDescription() );
        project.setEstimatedCost( request.getEstimatedCost() );
        project.setProjectType( request.getProjectType() );
        project.setStartDate( request.getStartDate() );
        project.setTitle( request.getTitle() );
        project.setVehicleId( request.getVehicleId() );

        return project;
    }

    @Override
    public ProjectResponse toResponse(Project project) {
        if ( project == null ) {
            return null;
        }

        ProjectResponse projectResponse = new ProjectResponse();

        projectResponse.setActualCost( project.getActualCost() );
        projectResponse.setCreatedAt( project.getCreatedAt() );
        projectResponse.setCustomerId( project.getCustomerId() );
        projectResponse.setDescription( project.getDescription() );
        projectResponse.setEndDate( project.getEndDate() );
        projectResponse.setEstimatedCost( project.getEstimatedCost() );
        projectResponse.setId( project.getId() );
        projectResponse.setProjectCode( project.getProjectCode() );
        projectResponse.setProjectType( project.getProjectType() );
        projectResponse.setStartDate( project.getStartDate() );
        projectResponse.setStatus( project.getStatus() );
        projectResponse.setTitle( project.getTitle() );
        projectResponse.setUpdatedAt( project.getUpdatedAt() );
        projectResponse.setVehicleId( project.getVehicleId() );

        return projectResponse;
    }

    @Override
    public void updateEntityFromRequest(UpdateProjectRequest request, Project project) {
        if ( request == null ) {
            return;
        }

        if ( request.getActualCost() != null ) {
            project.setActualCost( request.getActualCost() );
        }
        if ( request.getDescription() != null ) {
            project.setDescription( request.getDescription() );
        }
        if ( request.getEndDate() != null ) {
            project.setEndDate( request.getEndDate() );
        }
        if ( request.getEstimatedCost() != null ) {
            project.setEstimatedCost( request.getEstimatedCost() );
        }
        if ( request.getProjectType() != null ) {
            project.setProjectType( request.getProjectType() );
        }
        if ( request.getStartDate() != null ) {
            project.setStartDate( request.getStartDate() );
        }
        if ( request.getStatus() != null ) {
            project.setStatus( request.getStatus() );
        }
        if ( request.getTitle() != null ) {
            project.setTitle( request.getTitle() );
        }
    }
}
