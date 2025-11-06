package com.example.appointment_service.mapper;

import com.example.appointment_service.dto.*;
import com.example.appointment_service.entity.Appointment;
import org.springframework.stereotype.Component;

import java.time.ZoneOffset;

@Component
public class AppointmentMapper {

    public Appointment toEntity(CreateAppointmentRequest request) {
        return Appointment.builder()
                .customerId(request.getCustomerId())
                .vehicleId(request.getVehicleId())
                .technicianId(request.getTechnicianId())
                .scheduledStart(request.getScheduledStart() != null
                        ? request.getScheduledStart().atOffset(ZoneOffset.UTC)
                        : null)
                .scheduledEnd(request.getScheduledEnd() != null
                        ? request.getScheduledEnd().atOffset(ZoneOffset.UTC)
                        : null)
                .build();
    }

    public AppointmentResponse toResponse(Appointment entity) {
        AppointmentResponse response = new AppointmentResponse();
        response.setAppointmentId(entity.getAppointmentId());
        response.setCustomerId(entity.getCustomerId());
        response.setVehicleId(entity.getVehicleId());
        response.setTechnicianId(entity.getTechnicianId());
        response.setStatus(entity.getStatus());
        response.setScheduledStart(entity.getScheduledStart());
        response.setScheduledEnd(entity.getScheduledEnd());
        response.setCreatedAt(entity.getCreatedAt());
        response.setUpdatedAt(entity.getUpdatedAt());
        return response;
    }
}
