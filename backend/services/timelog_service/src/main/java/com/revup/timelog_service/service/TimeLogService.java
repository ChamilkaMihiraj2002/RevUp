package com.revup.timelog_service.service;

import com.revup.timelog_service.dto.CreateTimeLogRequest;
import com.revup.timelog_service.dto.TimeLogResponse;
import com.revup.timelog_service.dto.UpdateTimeLogRequest;
import com.revup.timelog_service.dto.WorkloadReportResponse;
import com.revup.timelog_service.entity.TimeLog;
import com.revup.timelog_service.exception.ResourceNotFoundException;
import com.revup.timelog_service.mapper.TimeLogMapper;
import com.revup.timelog_service.repository.TimeLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class TimeLogService {
    
    private final TimeLogRepository timeLogRepository;
    private final TimeLogMapper timeLogMapper;
    
    public Mono<TimeLogResponse> createTimeLog(CreateTimeLogRequest request) {
        log.info("Creating time log for employee: {}", request.getEmployeeId());
        
        TimeLog timeLog = timeLogMapper.toEntity(request);
        timeLog.setCreatedAt(LocalDateTime.now());
        timeLog.setUpdatedAt(LocalDateTime.now());
        
        return timeLogRepository.save(timeLog)
                .map(timeLogMapper::toResponse)
                .doOnSuccess(response -> log.info("Time log created with id: {}", response.getId()));
    }
    
    public Mono<TimeLogResponse> getTimeLogById(Long id) {
        return timeLogRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Time log not found with id: " + id)))
                .map(timeLogMapper::toResponse);
    }
    
    public Flux<TimeLogResponse> getAllTimeLogs() {
        return timeLogRepository.findAll()
                .map(timeLogMapper::toResponse);
    }
    
    public Flux<TimeLogResponse> getTimeLogsByEmployee(Long employeeId) {
        return timeLogRepository.findByEmployeeId(employeeId)
                .map(timeLogMapper::toResponse);
    }
    
    public Flux<TimeLogResponse> getTimeLogsByService(Long serviceInstanceId) {
        return timeLogRepository.findByServiceInstanceId(serviceInstanceId)
                .map(timeLogMapper::toResponse);
    }
    
    public Flux<TimeLogResponse> getTimeLogsByProject(Long projectId) {
        return timeLogRepository.findByProjectId(projectId)
                .map(timeLogMapper::toResponse);
    }
    
    public Flux<TimeLogResponse> getBillableTimeLogs() {
        return timeLogRepository.findByBillable(true)
                .map(timeLogMapper::toResponse);
    }
    
    public Flux<TimeLogResponse> getNonBillableTimeLogs() {
        return timeLogRepository.findByBillable(false)
                .map(timeLogMapper::toResponse);
    }
    
    public Flux<TimeLogResponse> getTimeLogsByDateRange(LocalDate startDate, LocalDate endDate) {
        return timeLogRepository.findByDateRange(startDate, endDate)
                .map(timeLogMapper::toResponse);
    }
    
    public Flux<TimeLogResponse> getEmployeeTimeLogsByDateRange(Long employeeId, LocalDate startDate, LocalDate endDate) {
        return timeLogRepository.findByEmployeeIdAndDateRange(employeeId, startDate, endDate)
                .map(timeLogMapper::toResponse);
    }
    
    public Mono<WorkloadReportResponse> getEmployeeWorkloadReport(Long employeeId, LocalDate startDate, LocalDate endDate) {
        log.info("Generating workload report for employee: {} from {} to {}", employeeId, startDate, endDate);
        
        return Mono.zip(
                timeLogRepository.sumHoursByEmployeeAndDateRange(employeeId, startDate, endDate),
                timeLogRepository.sumHoursByEmployeeBillableAndDateRange(employeeId, true, startDate, endDate),
                timeLogRepository.sumHoursByEmployeeBillableAndDateRange(employeeId, false, startDate, endDate),
                timeLogRepository.countDistinctDatesByEmployeeAndDateRange(employeeId, startDate, endDate)
        ).map(tuple -> {
            BigDecimal totalHours = tuple.getT1();
            BigDecimal billableHours = tuple.getT2();
            BigDecimal nonBillableHours = tuple.getT3();
            Long distinctDays = tuple.getT4();
            
            BigDecimal averageHours = BigDecimal.ZERO;
            if (distinctDays > 0) {
                averageHours = totalHours.divide(BigDecimal.valueOf(distinctDays), 2, RoundingMode.HALF_UP);
            }
            
            return new WorkloadReportResponse(
                    employeeId,
                    distinctDays.intValue(),
                    totalHours,
                    billableHours,
                    nonBillableHours,
                    averageHours
            );
        });
    }
    
    public Mono<TimeLogResponse> updateTimeLog(Long id, UpdateTimeLogRequest request) {
        log.info("Updating time log with id: {}", id);
        
        return timeLogRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Time log not found with id: " + id)))
                .flatMap(existingTimeLog -> {
                    timeLogMapper.updateEntityFromRequest(request, existingTimeLog);
                    return timeLogRepository.save(existingTimeLog);
                })
                .map(timeLogMapper::toResponse)
                .doOnSuccess(response -> log.info("Time log updated: {}", response.getId()));
    }
    
    public Mono<Void> deleteTimeLog(Long id) {
        return timeLogRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Time log not found with id: " + id)))
                .flatMap(timeLog -> timeLogRepository.deleteById(id));
    }
}
