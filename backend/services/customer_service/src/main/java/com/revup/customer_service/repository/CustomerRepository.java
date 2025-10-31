package com.revup.customer_service.repository;

import com.revup.customer_service.model.entity.Customer;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface CustomerRepository extends R2dbcRepository<Customer, Long> {
    
    Mono<Customer> findByUserId(Long userId);
    
    Mono<Customer> findByCustomerCode(String customerCode);
    
    Mono<Boolean> existsByUserId(Long userId);
    
    Mono<Boolean> existsByCustomerCode(String customerCode);
    
    @Query("SELECT COUNT(*) FROM customer_schema.customers")
    Mono<Long> countAllCustomers();
}
