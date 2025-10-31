package com.revup.timelog_service.mapper;

import com.revup.timelog_service.dto.CreateTimeLogRequest;
import com.revup.timelog_service.dto.TimeLogResponse;
import com.revup.timelog_service.dto.UpdateTimeLogRequest;
import com.revup.timelog_service.entity.TimeLog;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-10-31T12:55:05+0530",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.44.0.v20251001-1143, environment: Java 21.0.8 (Eclipse Adoptium)"
)
@Component
public class TimeLogMapperImpl implements TimeLogMapper {

    @Override
    public TimeLog toEntity(CreateTimeLogRequest request) {
        if ( request == null ) {
            return null;
        }

        TimeLog timeLog = new TimeLog();

        timeLog.setBillable( request.getBillable() );
        timeLog.setDescription( request.getDescription() );
        timeLog.setEmployeeId( request.getEmployeeId() );
        timeLog.setHoursWorked( request.getHoursWorked() );
        timeLog.setLogDate( request.getLogDate() );
        timeLog.setProjectId( request.getProjectId() );
        timeLog.setServiceInstanceId( request.getServiceInstanceId() );

        return timeLog;
    }

    @Override
    public TimeLogResponse toResponse(TimeLog timeLog) {
        if ( timeLog == null ) {
            return null;
        }

        TimeLogResponse timeLogResponse = new TimeLogResponse();

        timeLogResponse.setBillable( timeLog.getBillable() );
        timeLogResponse.setCreatedAt( timeLog.getCreatedAt() );
        timeLogResponse.setDescription( timeLog.getDescription() );
        timeLogResponse.setEmployeeId( timeLog.getEmployeeId() );
        timeLogResponse.setHoursWorked( timeLog.getHoursWorked() );
        timeLogResponse.setId( timeLog.getId() );
        timeLogResponse.setLogDate( timeLog.getLogDate() );
        timeLogResponse.setProjectId( timeLog.getProjectId() );
        timeLogResponse.setServiceInstanceId( timeLog.getServiceInstanceId() );
        timeLogResponse.setUpdatedAt( timeLog.getUpdatedAt() );

        return timeLogResponse;
    }

    @Override
    public void updateEntityFromRequest(UpdateTimeLogRequest request, TimeLog timeLog) {
        if ( request == null ) {
            return;
        }

        if ( request.getBillable() != null ) {
            timeLog.setBillable( request.getBillable() );
        }
        if ( request.getDescription() != null ) {
            timeLog.setDescription( request.getDescription() );
        }
        if ( request.getHoursWorked() != null ) {
            timeLog.setHoursWorked( request.getHoursWorked() );
        }
        if ( request.getLogDate() != null ) {
            timeLog.setLogDate( request.getLogDate() );
        }
    }
}
