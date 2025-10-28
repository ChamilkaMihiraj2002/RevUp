package com.revup.time_tracking_service.controller;

import com.revup.time_tracking_service.dto.TimeLogDTO;
import com.revup.time_tracking_service.service.TimeLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/timelogs") // Base path for this controller
@RequiredArgsConstructor
public class TimeLogController {

    private final TimeLogService timeLogService;

    // CREATE
    @PostMapping
    public ResponseEntity<TimeLogDTO> createTimeLog(@RequestBody TimeLogDTO timeLogDTO) {
        TimeLogDTO createdLog = timeLogService.createTimeLog(timeLogDTO);
        return new ResponseEntity<>(createdLog, HttpStatus.CREATED);
    }

    // READ (Get One)
    @GetMapping("/{id}")
    public ResponseEntity<TimeLogDTO> getTimeLogById(@PathVariable Long id) {
        TimeLogDTO timeLog = timeLogService.getTimeLogById(id);
        return ResponseEntity.ok(timeLog);
    }

    // READ (Get All)
    @GetMapping
    public ResponseEntity<List<TimeLogDTO>> getAllTimeLogs() {
        List<TimeLogDTO> timeLogs = timeLogService.getAllTimeLogs();
        return ResponseEntity.ok(timeLogs);
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<TimeLogDTO> updateTimeLog(@PathVariable Long id, @RequestBody TimeLogDTO timeLogDTO) {
        TimeLogDTO updatedLog = timeLogService.updateTimeLog(id, timeLogDTO);
        return ResponseEntity.ok(updatedLog);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTimeLog(@PathVariable Long id) {
        timeLogService.deleteTimeLog(id);
        return ResponseEntity.noContent().build(); // Standard 204 No Content response
    }
}