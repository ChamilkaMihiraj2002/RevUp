package com.revup.time_tracking_service.mapper;

import com.revup.time_tracking_service.dto.request.TimeLogRequest;
import com.revup.time_tracking_service.dto.response.TimeLogResponse;
import com.revup.time_tracking_service.entity.TimeLog;
import org.springframework.stereotype.Component;

@Component // Make this class a Spring-managed bean
public class TimeLogMapper {

    /**
     * Converts a TimeLog entity to a TimeLogResponse DTO.
     */
    public TimeLogResponse mapToResponse(TimeLog timeLog) {
        TimeLogResponse dto = new TimeLogResponse();
        dto.setTimelogId(timeLog.getTimelogId());
        dto.setAppointmentServiceId(timeLog.getAppointmentServiceId());
        dto.setUserId(timeLog.getUserId());
        dto.setStartTime(timeLog.getStartTime());
        dto.setEndTime(timeLog.getEndTime());
        dto.setDescription(timeLog.getDescription());
        dto.setLogDate(timeLog.getLogDate());
        dto.setCreatedAt(timeLog.getCreatedAt());
        dto.setUpdatedAt(timeLog.getUpdatedAt());
        dto.setActualMinutes(timeLog.getActualMinutes());
        return dto;
    }

    /**
     * Converts a TimeLogRequest DTO to a new TimeLog entity (for creation).
     */
    public TimeLog mapToEntity(TimeLogRequest dto) {
        TimeLog timeLog = new TimeLog();
        timeLog.setAppointmentServiceId(dto.getAppointmentServiceId());
        timeLog.setUserId(dto.getUserId());
        timeLog.setStartTime(dto.getStartTime());
        timeLog.setEndTime(dto.getEndTime());
        timeLog.setDescription(dto.getDescription());
        timeLog.setLogDate(dto.getLogDate());
        // createdAt and updatedAt are set automatically by auditing
        return timeLog;
    }

    /**
     * Updates an existing TimeLog entity from a TimeLogRequest DTO (for updates).
     */
    public void updateEntityFromRequest(TimeLog existingLog, TimeLogRequest dto) {
        existingLog.setAppointmentServiceId(dto.getAppointmentServiceId());
        existingLog.setUserId(dto.getUserId());
        existingLog.setStartTime(dto.getStartTime());
        existingLog.setEndTime(dto.getEndTime());
        existingLog.setDescription(dto.getDescription());
        existingLog.setLogDate(dto.getLogDate());
        // createdAt and updatedAt are handled automatically by auditing
    }
}