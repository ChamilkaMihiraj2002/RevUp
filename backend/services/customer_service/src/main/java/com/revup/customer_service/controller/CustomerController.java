package com.revup.customer_service.controller;

import com.revup.customer_service.model.dto.CreateCustomerRequest;
import com.revup.customer_service.model.dto.CustomerResponse;
import com.revup.customer_service.model.dto.UpdateCustomerRequest;
import com.revup.customer_service.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
@Tag(name = "Customer Management", description = "APIs for managing customer profiles")
public class CustomerController {
    
    private final CustomerService customerService;
    
    @PostMapping
    @Operation(summary = "Create customer", description = "Create a new customer profile")
    public Mono<ResponseEntity<CustomerResponse>> createCustomer(@Valid @RequestBody CreateCustomerRequest request) {
        return customerService.createCustomer(request)
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response));
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get customer by ID")
    public Mono<ResponseEntity<CustomerResponse>> getCustomerById(@PathVariable Long id) {
        return customerService.getCustomerById(id)
                .map(ResponseEntity::ok);
    }
    
    @GetMapping("/user/{userId}")
    @Operation(summary = "Get customer by user ID")
    public Mono<ResponseEntity<CustomerResponse>> getCustomerByUserId(@PathVariable Long userId) {
        return customerService.getCustomerByUserId(userId)
                .map(ResponseEntity::ok);
    }
    
    @GetMapping("/code/{customerCode}")
    @Operation(summary = "Get customer by customer code")
    public Mono<ResponseEntity<CustomerResponse>> getCustomerByCode(@PathVariable String customerCode) {
        return customerService.getCustomerByCode(customerCode)
                .map(ResponseEntity::ok);
    }
    
    @GetMapping
    @Operation(summary = "Get all customers")
    public Flux<CustomerResponse> getAllCustomers() {
        return customerService.getAllCustomers();
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update customer")
    public Mono<ResponseEntity<CustomerResponse>> updateCustomer(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCustomerRequest request) {
        return customerService.updateCustomer(id, request)
                .map(ResponseEntity::ok);
    }
    
    @PatchMapping("/{id}/loyalty-points")
    @Operation(summary = "Add loyalty points")
    public Mono<ResponseEntity<CustomerResponse>> addLoyaltyPoints(
            @PathVariable Long id,
            @RequestParam Integer points) {
        return customerService.addLoyaltyPoints(id, points)
                .map(ResponseEntity::ok);
    }
    
    @PatchMapping("/{id}/total-spent")
    @Operation(summary = "Update total spent")
    public Mono<ResponseEntity<CustomerResponse>> updateTotalSpent(
            @PathVariable Long id,
            @RequestParam BigDecimal amount) {
        return customerService.updateTotalSpent(id, amount)
                .map(ResponseEntity::ok);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete customer")
    public Mono<ResponseEntity<Void>> deleteCustomer(@PathVariable Long id) {
        return customerService.deleteCustomer(id)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()));
    }
}
