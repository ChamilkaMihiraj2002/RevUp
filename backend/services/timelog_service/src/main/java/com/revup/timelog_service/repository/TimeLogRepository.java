package com.revup.timelog_service.repository;

import com.revup.timelog_service.entity.TimeLog;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Repository
public interface TimeLogRepository extends R2dbcRepository<TimeLog, Long> {
    
    Flux<TimeLog> findByEmployeeId(Long employeeId);
    
    Flux<TimeLog> findByServiceInstanceId(Long serviceInstanceId);
    
    Flux<TimeLog> findByProjectId(Long projectId);
    
    Flux<TimeLog> findByBillable(Boolean billable);
    
    @Query("SELECT * FROM timelog_schema.time_logs WHERE employee_id = :employeeId AND log_date BETWEEN :startDate AND :endDate ORDER BY log_date DESC")
    Flux<TimeLog> findByEmployeeIdAndDateRange(Long employeeId, LocalDate startDate, LocalDate endDate);
    
    @Query("SELECT * FROM timelog_schema.time_logs WHERE log_date BETWEEN :startDate AND :endDate ORDER BY log_date DESC")
    Flux<TimeLog> findByDateRange(LocalDate startDate, LocalDate endDate);
    
    @Query("SELECT COALESCE(SUM(hours_worked), 0) FROM timelog_schema.time_logs WHERE employee_id = :employeeId AND log_date BETWEEN :startDate AND :endDate")
    Mono<java.math.BigDecimal> sumHoursByEmployeeAndDateRange(Long employeeId, LocalDate startDate, LocalDate endDate);
    
    @Query("SELECT COALESCE(SUM(hours_worked), 0) FROM timelog_schema.time_logs WHERE employee_id = :employeeId AND billable = :billable AND log_date BETWEEN :startDate AND :endDate")
    Mono<java.math.BigDecimal> sumHoursByEmployeeBillableAndDateRange(Long employeeId, Boolean billable, LocalDate startDate, LocalDate endDate);
    
    @Query("SELECT COUNT(DISTINCT log_date) FROM timelog_schema.time_logs WHERE employee_id = :employeeId AND log_date BETWEEN :startDate AND :endDate")
    Mono<Long> countDistinctDatesByEmployeeAndDateRange(Long employeeId, LocalDate startDate, LocalDate endDate);
}
