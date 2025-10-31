package com.revup.employee_service.controller;

import com.revup.employee_service.dto.CreateEmployeeRequest;
import com.revup.employee_service.dto.EmployeeResponse;
import com.revup.employee_service.dto.UpdateEmployeeRequest;
import com.revup.employee_service.model.AvailabilityStatus;
import com.revup.employee_service.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
@Tag(name = "Employee Management", description = "APIs for managing employee profiles")
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create new employee", description = "Creates a new employee profile")
    public Mono<EmployeeResponse> createEmployee(@Valid @RequestBody CreateEmployeeRequest request) {
        return employeeService.createEmployee(request);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get employee by ID", description = "Retrieves an employee by their ID")
    public Mono<EmployeeResponse> getEmployeeById(
            @Parameter(description = "Employee ID") @PathVariable Long id) {
        return employeeService.getEmployeeById(id);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get employee by user ID", description = "Retrieves an employee by their user ID")
    public Mono<EmployeeResponse> getEmployeeByUserId(
            @Parameter(description = "User ID") @PathVariable Long userId) {
        return employeeService.getEmployeeByUserId(userId);
    }

    @GetMapping("/code/{employeeCode}")
    @Operation(summary = "Get employee by employee code", description = "Retrieves an employee by their employee code")
    public Mono<EmployeeResponse> getEmployeeByCode(
            @Parameter(description = "Employee code") @PathVariable String employeeCode) {
        return employeeService.getEmployeeByCode(employeeCode);
    }

    @GetMapping
    @Operation(summary = "Get all employees", description = "Retrieves all employees")
    public Flux<EmployeeResponse> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @GetMapping("/availability/{status}")
    @Operation(summary = "Get employees by availability", description = "Retrieves employees by availability status")
    public Flux<EmployeeResponse> getEmployeesByAvailability(
            @Parameter(description = "Availability status") @PathVariable AvailabilityStatus status) {
        return employeeService.getEmployeesByAvailability(status);
    }

    @GetMapping("/specialization/{specialization}")
    @Operation(summary = "Get employees by specialization", description = "Retrieves employees by specialization")
    public Flux<EmployeeResponse> getEmployeesBySpecialization(
            @Parameter(description = "Specialization") @PathVariable String specialization) {
        return employeeService.getEmployeesBySpecialization(specialization);
    }

    @GetMapping("/top-rated")
    @Operation(summary = "Get top-rated employees", description = "Retrieves top-rated employees")
    public Flux<EmployeeResponse> getTopRatedEmployees(
            @Parameter(description = "Minimum rating") @RequestParam(defaultValue = "4.0") Double minRating) {
        return employeeService.getTopRatedEmployees(minRating);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update employee", description = "Updates an employee's information")
    public Mono<EmployeeResponse> updateEmployee(
            @Parameter(description = "Employee ID") @PathVariable Long id,
            @Valid @RequestBody UpdateEmployeeRequest request) {
        return employeeService.updateEmployee(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete employee", description = "Deletes an employee")
    public Mono<Void> deleteEmployee(
            @Parameter(description = "Employee ID") @PathVariable Long id) {
        return employeeService.deleteEmployee(id);
    }

    @PatchMapping("/{id}/availability")
    @Operation(summary = "Update availability status", description = "Updates an employee's availability status")
    public Mono<EmployeeResponse> updateAvailabilityStatus(
            @Parameter(description = "Employee ID") @PathVariable Long id,
            @Parameter(description = "Availability status") @RequestParam AvailabilityStatus status) {
        return employeeService.updateAvailabilityStatus(id, status);
    }

    @PostMapping("/{id}/services/increment")
    @Operation(summary = "Increment services completed", description = "Increments the total services completed counter")
    public Mono<EmployeeResponse> incrementServicesCompleted(
            @Parameter(description = "Employee ID") @PathVariable Long id) {
        return employeeService.incrementServicesCompleted(id);
    }

    @PostMapping("/{id}/rating")
    @Operation(summary = "Update employee rating", description = "Updates an employee's rating")
    public Mono<EmployeeResponse> updateRating(
            @Parameter(description = "Employee ID") @PathVariable Long id,
            @Parameter(description = "New rating") @RequestParam BigDecimal rating) {
        return employeeService.updateRating(id, rating);
    }
}
