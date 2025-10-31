package com.revup.appointment_service.mapper;

import com.revup.appointment_service.dto.AppointmentResponse;
import com.revup.appointment_service.dto.CreateAppointmentRequest;
import com.revup.appointment_service.dto.UpdateAppointmentRequest;
import com.revup.appointment_service.model.Appointment;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-10-31T12:54:43+0530",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.44.0.v20251001-1143, environment: Java 21.0.8 (Eclipse Adoptium)"
)
@Component
public class AppointmentMapperImpl implements AppointmentMapper {

    @Override
    public AppointmentResponse toResponse(Appointment appointment) {
        if ( appointment == null ) {
            return null;
        }

        AppointmentResponse appointmentResponse = new AppointmentResponse();

        appointmentResponse.setCreatedAt( appointment.getCreatedAt() );
        appointmentResponse.setCustomerId( appointment.getCustomerId() );
        appointmentResponse.setEmployeeId( appointment.getEmployeeId() );
        appointmentResponse.setEstimatedDuration( appointment.getEstimatedDuration() );
        appointmentResponse.setId( appointment.getId() );
        appointmentResponse.setNotes( appointment.getNotes() );
        appointmentResponse.setScheduledDate( appointment.getScheduledDate() );
        appointmentResponse.setServiceType( appointment.getServiceType() );
        appointmentResponse.setStatus( appointment.getStatus() );
        appointmentResponse.setUpdatedAt( appointment.getUpdatedAt() );
        appointmentResponse.setVehicleId( appointment.getVehicleId() );

        return appointmentResponse;
    }

    @Override
    public Appointment toEntity(CreateAppointmentRequest request) {
        if ( request == null ) {
            return null;
        }

        Appointment appointment = new Appointment();

        appointment.setCustomerId( request.getCustomerId() );
        appointment.setEmployeeId( request.getEmployeeId() );
        appointment.setEstimatedDuration( request.getEstimatedDuration() );
        appointment.setNotes( request.getNotes() );
        appointment.setScheduledDate( request.getScheduledDate() );
        appointment.setServiceType( request.getServiceType() );
        appointment.setVehicleId( request.getVehicleId() );

        return appointment;
    }

    @Override
    public void updateAppointmentFromRequest(UpdateAppointmentRequest request, Appointment appointment) {
        if ( request == null ) {
            return;
        }

        if ( request.getEmployeeId() != null ) {
            appointment.setEmployeeId( request.getEmployeeId() );
        }
        if ( request.getEstimatedDuration() != null ) {
            appointment.setEstimatedDuration( request.getEstimatedDuration() );
        }
        if ( request.getNotes() != null ) {
            appointment.setNotes( request.getNotes() );
        }
        if ( request.getScheduledDate() != null ) {
            appointment.setScheduledDate( request.getScheduledDate() );
        }
        if ( request.getStatus() != null ) {
            appointment.setStatus( request.getStatus() );
        }
    }
}
