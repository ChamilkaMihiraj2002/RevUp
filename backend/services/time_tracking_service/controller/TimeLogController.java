package com.revup.time_tracking_service.controller;


import com.revup.time_tracking_service.dto.TimeLogRequest;
import com.revup.time_tracking_service.dto.TimeLogResponse;
import com.revup.time_tracking_service.service.TimeLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/v1/timelogs")
@RequiredArgsConstructor
public class TimeLogController {

    private final TimeLogService service;

    @PostMapping
    public ResponseEntity<TimeLogResponse> logTime(
            @RequestBody @Valid TimeLogRequest request,
            @AuthenticationPrincipal Jwt jwt) {

        Long userId = Long.parseLong(jwt.getSubject());
        TimeLogResponse response = service.logTime(request, userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/appointment/{appointmentServiceId}")
    public List<TimeLogResponse> getByAppointmentService(@PathVariable Long appointmentServiceId) {
        // Add authorization if needed
        return service.getByAppointmentService(appointmentServiceId);
    }
}