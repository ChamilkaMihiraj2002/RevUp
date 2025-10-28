package com.revup.time_tracking_service.service;

import com.revup.time_tracking_service.dto.TimeLogDTO;
import java.util.List;

public interface TimeLogService {
    TimeLogDTO createTimeLog(TimeLogDTO timeLogDTO);
    TimeLogDTO getTimeLogById(Long id);
    List<TimeLogDTO> getAllTimeLogs();
    TimeLogDTO updateTimeLog(Long id, TimeLogDTO timeLogDTO);
    void deleteTimeLog(Long id);
}