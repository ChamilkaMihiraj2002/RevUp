package com.revup.appointment_service.mapper;

import com.revup.appointment_service.dto.AppointmentResponse;
import com.revup.appointment_service.dto.CreateAppointmentRequest;
import com.revup.appointment_service.dto.UpdateAppointmentRequest;
import com.revup.appointment_service.model.Appointment;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AppointmentMapper {

    AppointmentResponse toResponse(Appointment appointment);

    Appointment toEntity(CreateAppointmentRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateAppointmentFromRequest(UpdateAppointmentRequest request, @MappingTarget Appointment appointment);
}
