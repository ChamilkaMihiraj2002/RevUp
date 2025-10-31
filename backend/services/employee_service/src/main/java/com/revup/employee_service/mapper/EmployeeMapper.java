package com.revup.employee_service.mapper;

import com.revup.employee_service.dto.CreateEmployeeRequest;
import com.revup.employee_service.dto.EmployeeResponse;
import com.revup.employee_service.dto.UpdateEmployeeRequest;
import com.revup.employee_service.model.Employee;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface EmployeeMapper {

    EmployeeResponse toResponse(Employee employee);

    Employee toEntity(CreateEmployeeRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEmployeeFromRequest(UpdateEmployeeRequest request, @MappingTarget Employee employee);
}
