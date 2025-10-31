package com.revup.timelog_service.controller;

import com.revup.timelog_service.dto.CreateTimeLogRequest;
import com.revup.timelog_service.dto.TimeLogResponse;
import com.revup.timelog_service.dto.UpdateTimeLogRequest;
import com.revup.timelog_service.dto.WorkloadReportResponse;
import com.revup.timelog_service.service.TimeLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/timelogs")
@RequiredArgsConstructor
@Tag(name = "Time Log Management", description = "APIs for time logging and workload tracking")
public class TimeLogController {
    
    private final TimeLogService timeLogService;
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create new time log entry")
    public Mono<TimeLogResponse> createTimeLog(@Valid @RequestBody CreateTimeLogRequest request) {
        return timeLogService.createTimeLog(request);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get time log by ID")
    public Mono<TimeLogResponse> getTimeLogById(@PathVariable Long id) {
        return timeLogService.getTimeLogById(id);
    }
    
    @GetMapping
    @Operation(summary = "Get all time logs")
    public Flux<TimeLogResponse> getAllTimeLogs() {
        return timeLogService.getAllTimeLogs();
    }
    
    @GetMapping("/employee/{employeeId}")
    @Operation(summary = "Get time logs by employee")
    public Flux<TimeLogResponse> getTimeLogsByEmployee(@PathVariable Long employeeId) {
        return timeLogService.getTimeLogsByEmployee(employeeId);
    }
    
    @GetMapping("/service/{serviceInstanceId}")
    @Operation(summary = "Get time logs by service instance")
    public Flux<TimeLogResponse> getTimeLogsByService(@PathVariable Long serviceInstanceId) {
        return timeLogService.getTimeLogsByService(serviceInstanceId);
    }
    
    @GetMapping("/project/{projectId}")
    @Operation(summary = "Get time logs by project")
    public Flux<TimeLogResponse> getTimeLogsByProject(@PathVariable Long projectId) {
        return timeLogService.getTimeLogsByProject(projectId);
    }
    
    @GetMapping("/billable")
    @Operation(summary = "Get billable time logs")
    public Flux<TimeLogResponse> getBillableTimeLogs() {
        return timeLogService.getBillableTimeLogs();
    }
    
    @GetMapping("/non-billable")
    @Operation(summary = "Get non-billable time logs")
    public Flux<TimeLogResponse> getNonBillableTimeLogs() {
        return timeLogService.getNonBillableTimeLogs();
    }
    
    @GetMapping("/date-range")
    @Operation(summary = "Get time logs by date range")
    public Flux<TimeLogResponse> getTimeLogsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return timeLogService.getTimeLogsByDateRange(startDate, endDate);
    }
    
    @GetMapping("/employee/{employeeId}/date-range")
    @Operation(summary = "Get employee time logs by date range")
    public Flux<TimeLogResponse> getEmployeeTimeLogsByDateRange(
            @PathVariable Long employeeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return timeLogService.getEmployeeTimeLogsByDateRange(employeeId, startDate, endDate);
    }
    
    @GetMapping("/employee/{employeeId}/workload-report")
    @Operation(summary = "Get employee workload report")
    public Mono<WorkloadReportResponse> getEmployeeWorkloadReport(
            @PathVariable Long employeeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return timeLogService.getEmployeeWorkloadReport(employeeId, startDate, endDate);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update time log")
    public Mono<TimeLogResponse> updateTimeLog(@PathVariable Long id, @Valid @RequestBody UpdateTimeLogRequest request) {
        return timeLogService.updateTimeLog(id, request);
    }
    
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete time log")
    public Mono<Void> deleteTimeLog(@PathVariable Long id) {
        return timeLogService.deleteTimeLog(id);
    }
}
