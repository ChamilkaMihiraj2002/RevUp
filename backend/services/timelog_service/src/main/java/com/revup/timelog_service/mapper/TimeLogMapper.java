package com.revup.timelog_service.mapper;

import com.revup.timelog_service.dto.CreateTimeLogRequest;
import com.revup.timelog_service.dto.TimeLogResponse;
import com.revup.timelog_service.dto.UpdateTimeLogRequest;
import com.revup.timelog_service.entity.TimeLog;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TimeLogMapper {
    
    TimeLog toEntity(CreateTimeLogRequest request);
    
    TimeLogResponse toResponse(TimeLog timeLog);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "employeeId", ignore = true)
    @Mapping(target = "serviceInstanceId", ignore = true)
    @Mapping(target = "projectId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromRequest(UpdateTimeLogRequest request, @MappingTarget TimeLog timeLog);
}
