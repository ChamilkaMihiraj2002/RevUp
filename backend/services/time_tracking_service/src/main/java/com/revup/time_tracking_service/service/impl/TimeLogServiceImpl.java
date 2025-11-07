package com.revup.time_tracking_service.service.impl;

import com.revup.time_tracking_service.dto.request.TimeLogRequest;
import com.revup.time_tracking_service.dto.response.TimeLogResponse;
import com.revup.time_tracking_service.entity.TimeLog;
import com.revup.time_tracking_service.exception.ResourceNotFoundException;
import com.revup.time_tracking_service.mapper.TimeLogMapper;
import com.revup.time_tracking_service.repository.TimeLogRepository;
import com.revup.time_tracking_service.service.TimeLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers; // <-- Important

@Service
@RequiredArgsConstructor
public class TimeLogServiceImpl implements TimeLogService {

    private final TimeLogRepository timeLogRepository; // The blocking JPA repo
    private final TimeLogMapper timeLogMapper;

    @Override
    public Mono<TimeLogResponse> createTimeLog(TimeLogRequest request) {
        // Run this blocking code on a different thread
        return Mono.fromCallable(() -> {
            TimeLog timeLog = timeLogMapper.mapToEntity(request);
            TimeLog savedLog = timeLogRepository.save(timeLog); // BLOCKING CALL
            return timeLogMapper.mapToResponse(savedLog);
        }).subscribeOn(Schedulers.boundedElastic()); // <-- Moves the work
    }

    @Override
    public Mono<TimeLogResponse> getTimeLogById(Long id) {
        return Mono.fromCallable(() -> {
            TimeLog timeLog = timeLogRepository.findById(id) // BLOCKING CALL
                    .orElseThrow(() -> new ResourceNotFoundException("TimeLog not found with id: " + id));
            return timeLogMapper.mapToResponse(timeLog);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Flux<TimeLogResponse> getAllTimeLogs() {
        // For a List, we use fromCallable and then flatten it into a Flux
        return Mono.fromCallable(() -> 
            timeLogRepository.findAll().stream() // BLOCKING CALL
                    .map(timeLogMapper::mapToResponse)
                    .toList()
        )
        .flatMapMany(Flux::fromIterable) // Convert List<DTO> to Flux<DTO>
        .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<TimeLogResponse> updateTimeLog(Long id, TimeLogRequest request) {
        return Mono.fromCallable(() -> {
            TimeLog existingLog = timeLogRepository.findById(id) // BLOCKING CALL
                    .orElseThrow(() -> new ResourceNotFoundException("TimeLog not found with id: " + id));
            
            timeLogMapper.updateEntityFromRequest(existingLog, request);
            TimeLog updatedLog = timeLogRepository.save(existingLog); // BLOCKING CALL
            return timeLogMapper.mapToResponse(updatedLog);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<TimeLogResponse> getTimeLogByAppointmentServiceAndUser(Long appointmentServiceId, Long userId) {
        return Mono.fromCallable(() -> {
            TimeLog timeLog = timeLogRepository.findByAppointmentServiceIdAndUserId(appointmentServiceId, userId)
                    .orElseThrow(() -> new ResourceNotFoundException("TimeLog not found with appointmentServiceId: "
                            + appointmentServiceId + " and userId: " + userId));
            return timeLogMapper.mapToResponse(timeLog);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<TimeLogResponse> updateTimeLogByAppointmentServiceAndUser(Long appointmentServiceId, Long userId, TimeLogRequest request) {
        return Mono.fromCallable(() -> {
            TimeLog existingLog = timeLogRepository.findByAppointmentServiceIdAndUserId(appointmentServiceId, userId)
                    .orElseThrow(() -> new ResourceNotFoundException("TimeLog not found with appointmentServiceId: "
                            + appointmentServiceId + " and userId: " + userId));

            timeLogMapper.updateEntityFromRequest(existingLog, request);
            TimeLog updatedLog = timeLogRepository.save(existingLog); // BLOCKING CALL
            return timeLogMapper.mapToResponse(updatedLog);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<Void> deleteTimeLog(Long id) {
        // For void methods, we use .then()
        return Mono.fromRunnable(() -> {
            if (!timeLogRepository.existsById(id)) { // BLOCKING CALL
                throw new ResourceNotFoundException("TimeLog not found with id: " + id);
            }
            timeLogRepository.deleteById(id); // BLOCKING CALL
        })
        .subscribeOn(Schedulers.boundedElastic())
        .then();
    }
}