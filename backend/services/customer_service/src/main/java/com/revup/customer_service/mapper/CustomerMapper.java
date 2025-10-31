package com.revup.customer_service.mapper;

import com.revup.customer_service.model.dto.CreateCustomerRequest;
import com.revup.customer_service.model.dto.CustomerResponse;
import com.revup.customer_service.model.dto.UpdateCustomerRequest;
import com.revup.customer_service.model.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CustomerMapper {
    
    Customer toEntity(CreateCustomerRequest request);
    
    CustomerResponse toResponse(Customer customer);
    
    void updateEntityFromRequest(UpdateCustomerRequest request, @MappingTarget Customer customer);
}
