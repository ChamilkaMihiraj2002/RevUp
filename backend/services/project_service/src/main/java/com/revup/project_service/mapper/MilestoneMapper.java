package com.revup.project_service.mapper;

import com.revup.project_service.dto.CreateMilestoneRequest;
import com.revup.project_service.dto.MilestoneResponse;
import com.revup.project_service.dto.UpdateMilestoneRequest;
import com.revup.project_service.entity.ProjectMilestone;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface MilestoneMapper {
    
    ProjectMilestone toEntity(CreateMilestoneRequest request);
    
    MilestoneResponse toResponse(ProjectMilestone milestone);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "projectId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromRequest(UpdateMilestoneRequest request, @MappingTarget ProjectMilestone milestone);
}
