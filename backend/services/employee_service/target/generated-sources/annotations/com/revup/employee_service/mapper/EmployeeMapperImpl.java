package com.revup.employee_service.mapper;

import com.revup.employee_service.dto.CreateEmployeeRequest;
import com.revup.employee_service.dto.EmployeeResponse;
import com.revup.employee_service.dto.UpdateEmployeeRequest;
import com.revup.employee_service.model.Employee;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-10-31T12:54:51+0530",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.44.0.v20251001-1143, environment: Java 21.0.8 (Eclipse Adoptium)"
)
@Component
public class EmployeeMapperImpl implements EmployeeMapper {

    @Override
    public EmployeeResponse toResponse(Employee employee) {
        if ( employee == null ) {
            return null;
        }

        EmployeeResponse employeeResponse = new EmployeeResponse();

        employeeResponse.setAvailabilityStatus( employee.getAvailabilityStatus() );
        employeeResponse.setAverageRating( employee.getAverageRating() );
        employeeResponse.setCreatedAt( employee.getCreatedAt() );
        employeeResponse.setEmployeeCode( employee.getEmployeeCode() );
        employeeResponse.setHireDate( employee.getHireDate() );
        employeeResponse.setHourlyRate( employee.getHourlyRate() );
        employeeResponse.setId( employee.getId() );
        employeeResponse.setSpecialization( employee.getSpecialization() );
        employeeResponse.setTotalServicesCompleted( employee.getTotalServicesCompleted() );
        employeeResponse.setUpdatedAt( employee.getUpdatedAt() );
        employeeResponse.setUserId( employee.getUserId() );

        return employeeResponse;
    }

    @Override
    public Employee toEntity(CreateEmployeeRequest request) {
        if ( request == null ) {
            return null;
        }

        Employee employee = new Employee();

        employee.setAvailabilityStatus( request.getAvailabilityStatus() );
        employee.setHireDate( request.getHireDate() );
        employee.setHourlyRate( request.getHourlyRate() );
        employee.setSpecialization( request.getSpecialization() );
        employee.setUserId( request.getUserId() );

        return employee;
    }

    @Override
    public void updateEmployeeFromRequest(UpdateEmployeeRequest request, Employee employee) {
        if ( request == null ) {
            return;
        }

        if ( request.getAvailabilityStatus() != null ) {
            employee.setAvailabilityStatus( request.getAvailabilityStatus() );
        }
        if ( request.getHireDate() != null ) {
            employee.setHireDate( request.getHireDate() );
        }
        if ( request.getHourlyRate() != null ) {
            employee.setHourlyRate( request.getHourlyRate() );
        }
        if ( request.getSpecialization() != null ) {
            employee.setSpecialization( request.getSpecialization() );
        }
    }
}
