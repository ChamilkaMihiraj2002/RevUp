package com.revup.project_service.mapper;

import com.revup.project_service.dto.CreateProjectRequest;
import com.revup.project_service.dto.ProjectResponse;
import com.revup.project_service.dto.UpdateProjectRequest;
import com.revup.project_service.entity.Project;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProjectMapper {
    
    Project toEntity(CreateProjectRequest request);
    
    ProjectResponse toResponse(Project project);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "projectCode", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromRequest(UpdateProjectRequest request, @MappingTarget Project project);
}
