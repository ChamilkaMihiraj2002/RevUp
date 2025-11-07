package com.example.project_service.mapper;

import com.example.project_service.dto.request.CreateProjectRequest;
import com.example.project_service.dto.request.UpdateProjectRequest;
import com.example.project_service.dto.ProjectDto;
import com.example.project_service.entity.Project;
import com.example.project_service.Enum.ProjectStatus;
import org.springframework.stereotype.Component;

@Component
public class ProjectMapper {

    public ProjectDto toDto(Project project) {
        ProjectDto dto = new ProjectDto();
        dto.setProjectId(project.getProjectId());
        dto.setUserId(project.getUserId());
        dto.setVehicleId(project.getVehicleId());
        dto.setTechnicianId(project.getTechnicianId());
        dto.setDescription(project.getDescription());
        dto.setStatus(project.getStatus());
        dto.setEstimateTime(project.getEstimateTime());
        dto.setEstimatedAmount(project.getEstimatedAmount());
        dto.setStartTime(project.getStartTime());
        dto.setEndTime(project.getEndTime());
        dto.setCreatedAt(project.getCreatedAt());
        dto.setUpdatedAt(project.getUpdatedAt());
        return dto;
    }

    public Project toEntity(CreateProjectRequest request) {
        Project project = new Project();
        project.setUserId(request.getUserId());
        project.setVehicleId(request.getVehicleId());
        project.setDescription(request.getDescription());
        
        if (request.getStatus() != null && !request.getStatus().isEmpty()) {
            project.setStatus(ProjectStatus.valueOf(request.getStatus().toUpperCase()));
        } else {
            project.setStatus(ProjectStatus.PENDING);
        }
        
        project.setEstimateTime(request.getEstimateTime());
        project.setStartTime(request.getStartTime());
        project.setEndTime(request.getEndTime());
        return project;
    }

    public void updateEntityFromDto(Project project, UpdateProjectRequest request) {
        if (request.getDescription() != null) {
            project.setDescription(request.getDescription());
        }
        if (request.getStatus() != null) {
            project.setStatus(ProjectStatus.valueOf(request.getStatus().toUpperCase()));
        }
        if (request.getEstimateTime() != null) {
            project.setEstimateTime(request.getEstimateTime());
        }
        if (request.getStartTime() != null) {
            project.setStartTime(request.getStartTime());
        }
        if (request.getEndTime() != null) {
            project.setEndTime(request.getEndTime());
        }
    }
}
