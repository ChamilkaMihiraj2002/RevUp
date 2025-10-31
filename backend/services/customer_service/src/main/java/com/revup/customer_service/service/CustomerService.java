package com.revup.customer_service.service;

import com.revup.customer_service.exception.DuplicateResourceException;
import com.revup.customer_service.exception.ResourceNotFoundException;
import com.revup.customer_service.mapper.CustomerMapper;
import com.revup.customer_service.model.dto.CreateCustomerRequest;
import com.revup.customer_service.model.dto.CustomerResponse;
import com.revup.customer_service.model.dto.UpdateCustomerRequest;
import com.revup.customer_service.model.entity.Customer;
import com.revup.customer_service.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerService {
    
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    
    @Transactional
    public Mono<CustomerResponse> createCustomer(CreateCustomerRequest request) {
        log.info("Creating customer for user ID: {}", request.getUserId());
        
        return customerRepository.existsByUserId(request.getUserId())
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new DuplicateResourceException(
                                "Customer already exists for user ID: " + request.getUserId()));
                    }
                    
                    Customer customer = customerMapper.toEntity(request);
                    customer.setCustomerCode(generateCustomerCode());
                    customer.setLoyaltyPoints(0);
                    customer.setTotalSpent(BigDecimal.ZERO);
                    customer.setCreatedAt(LocalDateTime.now());
                    customer.setUpdatedAt(LocalDateTime.now());
                    
                    return customerRepository.save(customer);
                })
                .map(customerMapper::toResponse)
                .doOnSuccess(response -> log.info("Customer created: {}", response.getCustomerCode()))
                .doOnError(error -> log.error("Error creating customer: {}", error.getMessage()));
    }
    
    public Mono<CustomerResponse> getCustomerById(Long id) {
        return customerRepository.findById(id)
                .map(customerMapper::toResponse)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Customer not found with ID: " + id)));
    }
    
    public Mono<CustomerResponse> getCustomerByUserId(Long userId) {
        return customerRepository.findByUserId(userId)
                .map(customerMapper::toResponse)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Customer not found for user ID: " + userId)));
    }
    
    public Mono<CustomerResponse> getCustomerByCode(String customerCode) {
        return customerRepository.findByCustomerCode(customerCode)
                .map(customerMapper::toResponse)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Customer not found with code: " + customerCode)));
    }
    
    public Flux<CustomerResponse> getAllCustomers() {
        return customerRepository.findAll()
                .map(customerMapper::toResponse);
    }
    
    @Transactional
    public Mono<CustomerResponse> updateCustomer(Long id, UpdateCustomerRequest request) {
        log.info("Updating customer: {}", id);
        
        return customerRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Customer not found with ID: " + id)))
                .flatMap(customer -> {
                    customerMapper.updateEntityFromRequest(request, customer);
                    customer.setUpdatedAt(LocalDateTime.now());
                    return customerRepository.save(customer);
                })
                .map(customerMapper::toResponse);
    }
    
    @Transactional
    public Mono<CustomerResponse> addLoyaltyPoints(Long id, Integer points) {
        return customerRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Customer not found")))
                .flatMap(customer -> {
                    customer.setLoyaltyPoints(customer.getLoyaltyPoints() + points);
                    customer.setUpdatedAt(LocalDateTime.now());
                    return customerRepository.save(customer);
                })
                .map(customerMapper::toResponse);
    }
    
    @Transactional
    public Mono<CustomerResponse> updateTotalSpent(Long id, BigDecimal amount) {
        return customerRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Customer not found")))
                .flatMap(customer -> {
                    customer.setTotalSpent(customer.getTotalSpent().add(amount));
                    customer.setUpdatedAt(LocalDateTime.now());
                    return customerRepository.save(customer);
                })
                .map(customerMapper::toResponse);
    }
    
    @Transactional
    public Mono<Void> deleteCustomer(Long id) {
        return customerRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Customer not found")))
                .flatMap(customerRepository::delete);
    }
    
    private String generateCustomerCode() {
        return "CUST-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
