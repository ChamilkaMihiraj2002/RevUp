package com.revup.time_tracking_service.service;

import com.revup.time_tracking_service.dto.request.TimeLogRequest;
import com.revup.time_tracking_service.dto.response.TimeLogResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TimeLogService {

    /**
     * Create a new time log.
     * @param request The DTO containing time log creation data.
     * @return A Mono emitting the created time log response.
     */
    Mono<TimeLogResponse> createTimeLog(TimeLogRequest request);

    /**
     * Get a single time log by its ID.
     * @param id The ID of the time log.
     * @return A Mono emitting the found time log, or empty if not found.
     */
    Mono<TimeLogResponse> getTimeLogById(Long id);

    /**
     * Get all time logs.
     * @return A Flux emitting all time logs.
     */
    Flux<TimeLogResponse> getAllTimeLogs();

    /**
     * Get a time log by appointmentServiceId and userId.
     */
    Mono<TimeLogResponse> getTimeLogByAppointmentServiceAndUser(Long appointmentServiceId, Long userId);

    /**
     * Update a time log identified by appointmentServiceId and userId.
     */
    Mono<TimeLogResponse> updateTimeLogByAppointmentServiceAndUser(Long appointmentServiceId, Long userId, TimeLogRequest request);

    /**
     * Update an existing time log.
     * @param id The ID of the time log to update.
     * @param request The DTO containing the update data.
     * @return A Mono emitting the updated time log.
     */
    Mono<TimeLogResponse> updateTimeLog(Long id, TimeLogRequest request);

    /**
     * Delete a time log by its ID.
     * @param id The ID of the time log to delete.
     * @return A Mono that completes when the deletion is done.
     */
    Mono<Void> deleteTimeLog(Long id);
}