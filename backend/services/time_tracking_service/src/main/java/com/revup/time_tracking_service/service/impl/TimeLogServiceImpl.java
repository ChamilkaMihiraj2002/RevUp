package com.revup.time_tracking_service.service.impl;

import com.revup.time_tracking_service.dto.TimeLogDTO;
import com.revup.time_tracking_service.entity.TimeLog;
import com.revup.time_tracking_service.exception.ResourceNotFoundException;
import com.revup.time_tracking_service.repository.TimeLogRepository;
import com.revup.time_tracking_service.service.TimeLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor // Lombok for constructor-based dependency injection
public class TimeLogServiceImpl implements TimeLogService {

    private final TimeLogRepository timeLogRepository;

    @Override
    public TimeLogDTO createTimeLog(TimeLogDTO timeLogDTO) {
        TimeLog timeLog = convertToEntity(timeLogDTO);
        TimeLog savedLog = timeLogRepository.save(timeLog);
        return convertToDTO(savedLog);
    }

    @Override
    public TimeLogDTO getTimeLogById(Long id) {
        TimeLog timeLog = timeLogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TimeLog not found with id: " + id));
        return convertToDTO(timeLog);
    }

    @Override
    public List<TimeLogDTO> getAllTimeLogs() {
        return timeLogRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TimeLogDTO updateTimeLog(Long id, TimeLogDTO timeLogDTO) {
        TimeLog existingLog = timeLogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TimeLog not found with id: " + id));

        // Update fields
        existingLog.setAppointmentServiceId(timeLogDTO.getAppointmentServiceId());
        existingLog.setUserId(timeLogDTO.getUserId());
        existingLog.setStartTime(timeLogDTO.getStartTime());
        existingLog.setEndTime(timeLogDTO.getEndTime());
        existingLog.setDescription(timeLogDTO.getDescription());
        existingLog.setLogDate(timeLogDTO.getLogDate());
        // createdAt and updatedAt are handled automatically by auditing

        TimeLog updatedLog = timeLogRepository.save(existingLog);
        return convertToDTO(updatedLog);
    }

    @Override
    public void deleteTimeLog(Long id) {
        if (!timeLogRepository.existsById(id)) {
            throw new ResourceNotFoundException("TimeLog not found with id: " + id);
        }
        timeLogRepository.deleteById(id);
    }

    // --- Helper Methods for Mapping ---

    private TimeLogDTO convertToDTO(TimeLog timeLog) {
        TimeLogDTO dto = new TimeLogDTO();
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

    private TimeLog convertToEntity(TimeLogDTO dto) {
        TimeLog timeLog = new TimeLog();
        // We don't set the ID for a new entity
        timeLog.setAppointmentServiceId(dto.getAppointmentServiceId());
        timeLog.setUserId(dto.getUserId());
        timeLog.setStartTime(dto.getStartTime());
        timeLog.setEndTime(dto.getEndTime());
        timeLog.setDescription(dto.getDescription());
        timeLog.setLogDate(dto.getLogDate());
        // createdAt and updatedAt are set automatically
        return timeLog;
    }
}