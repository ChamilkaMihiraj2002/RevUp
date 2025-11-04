package com.example.appointment_service.mapper;

import com.example.appointment_service.dto.CreateAppointmentServiceRequest;
import com.example.appointment_service.dto.AppointmentServiceResponse;
import com.example.appointment_service.entity.Appointment;
import com.example.appointment_service.entity.AppointmentService;

public class AppointmentServiceMapper {

    public static AppointmentService toEntity(CreateAppointmentServiceRequest dto, Appointment appointment) {
        return AppointmentService.builder()
                .actualMinutes(dto.getActualMinutes())
                .status(dto.getStatus())
                .quantity(dto.getQuantity())
                .estimatedMinutes(dto.getEstimatedMinutes())
                .appointment(appointment)
                .serviceTypeId(dto.getServiceTypeId())
                .build();
    }

    public static AppointmentServiceResponse toResponse(AppointmentService entity) {
        return AppointmentServiceResponse.builder()
                .appointmentServiceId(entity.getAppointmentServiceId())
                .actualMinutes(entity.getActualMinutes())
                .status(entity.getStatus())
                .quantity(entity.getQuantity())
                .estimatedMinutes(entity.getEstimatedMinutes())
                .appointmentId(entity.getAppointment().getAppointmentId())
                .serviceTypeId(entity.getServiceTypeId())
                .build();
    }
}
