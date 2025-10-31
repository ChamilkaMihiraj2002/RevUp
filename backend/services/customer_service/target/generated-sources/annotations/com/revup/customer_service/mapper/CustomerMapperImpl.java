package com.revup.customer_service.mapper;

import com.revup.customer_service.model.dto.CreateCustomerRequest;
import com.revup.customer_service.model.dto.CustomerResponse;
import com.revup.customer_service.model.dto.UpdateCustomerRequest;
import com.revup.customer_service.model.entity.Customer;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-10-31T12:54:48+0530",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.44.0.v20251001-1143, environment: Java 21.0.8 (Eclipse Adoptium)"
)
@Component
public class CustomerMapperImpl implements CustomerMapper {

    @Override
    public Customer toEntity(CreateCustomerRequest request) {
        if ( request == null ) {
            return null;
        }

        Customer.CustomerBuilder customer = Customer.builder();

        customer.companyName( request.getCompanyName() );
        customer.notes( request.getNotes() );
        customer.preferences( request.getPreferences() );
        customer.userId( request.getUserId() );

        return customer.build();
    }

    @Override
    public CustomerResponse toResponse(Customer customer) {
        if ( customer == null ) {
            return null;
        }

        CustomerResponse.CustomerResponseBuilder customerResponse = CustomerResponse.builder();

        customerResponse.companyName( customer.getCompanyName() );
        customerResponse.createdAt( customer.getCreatedAt() );
        customerResponse.customerCode( customer.getCustomerCode() );
        customerResponse.id( customer.getId() );
        customerResponse.loyaltyPoints( customer.getLoyaltyPoints() );
        customerResponse.notes( customer.getNotes() );
        customerResponse.preferences( customer.getPreferences() );
        customerResponse.totalSpent( customer.getTotalSpent() );
        customerResponse.updatedAt( customer.getUpdatedAt() );
        customerResponse.userId( customer.getUserId() );

        return customerResponse.build();
    }

    @Override
    public void updateEntityFromRequest(UpdateCustomerRequest request, Customer customer) {
        if ( request == null ) {
            return;
        }

        if ( request.getCompanyName() != null ) {
            customer.setCompanyName( request.getCompanyName() );
        }
        if ( request.getNotes() != null ) {
            customer.setNotes( request.getNotes() );
        }
        if ( request.getPreferences() != null ) {
            customer.setPreferences( request.getPreferences() );
        }
    }
}
