package com.revup.time_tracking_service.controller;

import com.revup.time_tracking_service.dto.request.TimeLogRequest;
import com.revup.time_tracking_service.dto.response.TimeLogResponse;
import com.revup.time_tracking_service.service.TimeLogService;
import jakarta.validation.Valid; // Use jakarta validation
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux; // Import Flux
import reactor.core.publisher.Mono;  // Import Mono

@Slf4j // For logging
@RestController
@RequestMapping("/api/v1/timelogs")
public class TimeLogController {

    private final TimeLogService timeLogService;

    // Use constructor injection as in your example
    public TimeLogController(TimeLogService timeLogService) {
        this.timeLogService = timeLogService;
    }

    // CREATE
    @PostMapping
    public Mono<ResponseEntity<TimeLogResponse>> createTimeLog(@Valid @RequestBody TimeLogRequest request) {
        log.info("Creating new time log for user: {}", request.getUserId());
        return timeLogService.createTimeLog(request)
                .map(createdLog -> new ResponseEntity<>(createdLog, HttpStatus.CREATED))
                .doOnSuccess(response -> log.info("Time log created successfully with id: {}", response.getBody().getTimelogId()));
    }

    // READ (Get One)
    @GetMapping("/{id}")
    public Mono<ResponseEntity<TimeLogResponse>> getTimeLogById(@PathVariable Long id) {
        log.info("Fetching time log with id: {}", id);
        return timeLogService.getTimeLogById(id)
                .map(ResponseEntity::ok);
    }

    // READ (Get All)
    @GetMapping
    public Flux<TimeLogResponse> getAllTimeLogs() {
        log.info("Fetching all time logs");
        return timeLogService.getAllTimeLogs();
    }

    // UPDATE
    @PutMapping("/{id}")
    public Mono<ResponseEntity<TimeLogResponse>> updateTimeLog(
            @PathVariable Long id,
            @Valid @RequestBody TimeLogRequest request) {
        log.info("Updating time log with id: {}", id);
        return timeLogService.updateTimeLog(id, request)
                .map(ResponseEntity::ok)
                .doOnSuccess(response -> log.info("Time log updated successfully with id: {}", id));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteTimeLog(@PathVariable Long id) {
        log.info("Deleting time log with id: {}", id);
        return timeLogService.deleteTimeLog(id)
                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)))
                .doOnSuccess(response -> log.info("Time log deleted successfully with id: {}", id));
    }
}