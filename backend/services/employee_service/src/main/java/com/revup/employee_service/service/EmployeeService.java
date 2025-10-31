package com.revup.employee_service.service;

import com.revup.employee_service.dto.CreateEmployeeRequest;
import com.revup.employee_service.dto.EmployeeResponse;
import com.revup.employee_service.dto.UpdateEmployeeRequest;
import com.revup.employee_service.exception.DuplicateResourceException;
import com.revup.employee_service.exception.ResourceNotFoundException;
import com.revup.employee_service.mapper.EmployeeMapper;
import com.revup.employee_service.model.AvailabilityStatus;
import com.revup.employee_service.model.Employee;
import com.revup.employee_service.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final Random random = new Random();

    @Transactional
    public Mono<EmployeeResponse> createEmployee(CreateEmployeeRequest request) {
        log.info("Creating employee for user ID: {}", request.getUserId());

        return employeeRepository.existsByUserId(request.getUserId())
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new DuplicateResourceException(
                                "Employee profile already exists for user ID: " + request.getUserId()));
                    }
                    return generateUniqueEmployeeCode()
                            .flatMap(code -> {
                                Employee employee = employeeMapper.toEntity(request);
                                employee.setEmployeeCode(code);
                                employee.setAvailabilityStatus(
                                        request.getAvailabilityStatus() != null ?
                                                request.getAvailabilityStatus() : AvailabilityStatus.AVAILABLE
                                );
                                employee.setTotalServicesCompleted(0);
                                employee.setAverageRating(BigDecimal.ZERO);
                                employee.setCreatedAt(LocalDateTime.now());
                                employee.setUpdatedAt(LocalDateTime.now());
                                return employeeRepository.save(employee);
                            })
                            .map(employeeMapper::toResponse);
                });
    }

    public Mono<EmployeeResponse> getEmployeeById(Long id) {
        log.info("Fetching employee with ID: {}", id);
        return employeeRepository.findById(id)
                .map(employeeMapper::toResponse)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Employee not found with ID: " + id)));
    }

    public Mono<EmployeeResponse> getEmployeeByUserId(Long userId) {
        log.info("Fetching employee for user ID: {}", userId);
        return employeeRepository.findByUserId(userId)
                .map(employeeMapper::toResponse)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Employee not found for user ID: " + userId)));
    }

    public Mono<EmployeeResponse> getEmployeeByCode(String employeeCode) {
        log.info("Fetching employee with code: {}", employeeCode);
        return employeeRepository.findByEmployeeCode(employeeCode)
                .map(employeeMapper::toResponse)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Employee not found with code: " + employeeCode)));
    }

    public Flux<EmployeeResponse> getAllEmployees() {
        log.info("Fetching all employees");
        return employeeRepository.findAll()
                .map(employeeMapper::toResponse);
    }

    public Flux<EmployeeResponse> getEmployeesByAvailability(AvailabilityStatus status) {
        log.info("Fetching employees with availability status: {}", status);
        return employeeRepository.findByAvailabilityStatus(status)
                .map(employeeMapper::toResponse);
    }

    public Flux<EmployeeResponse> getEmployeesBySpecialization(String specialization) {
        log.info("Fetching employees with specialization: {}", specialization);
        return employeeRepository.findBySpecializationContainingIgnoreCase(specialization)
                .map(employeeMapper::toResponse);
    }

    public Flux<EmployeeResponse> getTopRatedEmployees(Double minRating) {
        log.info("Fetching top-rated employees with minimum rating: {}", minRating);
        return employeeRepository.findTopRatedEmployees(minRating)
                .map(employeeMapper::toResponse);
    }

    @Transactional
    public Mono<EmployeeResponse> updateEmployee(Long id, UpdateEmployeeRequest request) {
        log.info("Updating employee with ID: {}", id);
        return employeeRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Employee not found with ID: " + id)))
                .flatMap(employee -> {
                    employeeMapper.updateEmployeeFromRequest(request, employee);
                    employee.setUpdatedAt(LocalDateTime.now());
                    return employeeRepository.save(employee);
                })
                .map(employeeMapper::toResponse);
    }

    @Transactional
    public Mono<Void> deleteEmployee(Long id) {
        log.info("Deleting employee with ID: {}", id);
        return employeeRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Employee not found with ID: " + id)))
                .flatMap(employeeRepository::delete);
    }

    @Transactional
    public Mono<EmployeeResponse> updateAvailabilityStatus(Long id, AvailabilityStatus status) {
        log.info("Updating availability status for employee ID: {} to {}", id, status);
        return employeeRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Employee not found with ID: " + id)))
                .flatMap(employee -> {
                    employee.setAvailabilityStatus(status);
                    employee.setUpdatedAt(LocalDateTime.now());
                    return employeeRepository.save(employee);
                })
                .map(employeeMapper::toResponse);
    }

    @Transactional
    public Mono<EmployeeResponse> incrementServicesCompleted(Long id) {
        log.info("Incrementing services completed for employee ID: {}", id);
        return employeeRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Employee not found with ID: " + id)))
                .flatMap(employee -> {
                    employee.setTotalServicesCompleted(employee.getTotalServicesCompleted() + 1);
                    employee.setUpdatedAt(LocalDateTime.now());
                    return employeeRepository.save(employee);
                })
                .map(employeeMapper::toResponse);
    }

    @Transactional
    public Mono<EmployeeResponse> updateRating(Long id, BigDecimal newRating) {
        log.info("Updating rating for employee ID: {} with new rating: {}", id, newRating);
        return employeeRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Employee not found with ID: " + id)))
                .flatMap(employee -> {
                    // Calculate new average rating
                    BigDecimal currentAvg = employee.getAverageRating();
                    int totalServices = employee.getTotalServicesCompleted();
                    
                    if (totalServices == 0) {
                        employee.setAverageRating(newRating);
                    } else {
                        BigDecimal totalRating = currentAvg.multiply(BigDecimal.valueOf(totalServices));
                        BigDecimal newTotal = totalRating.add(newRating);
                        BigDecimal newAverage = newTotal.divide(BigDecimal.valueOf(totalServices + 1), 2, BigDecimal.ROUND_HALF_UP);
                        employee.setAverageRating(newAverage);
                    }
                    
                    employee.setUpdatedAt(LocalDateTime.now());
                    return employeeRepository.save(employee);
                })
                .map(employeeMapper::toResponse);
    }

    private Mono<String> generateUniqueEmployeeCode() {
        String code = String.format("EMP-%08d", random.nextInt(100000000));
        return employeeRepository.existsByEmployeeCode(code)
                .flatMap(exists -> exists ? generateUniqueEmployeeCode() : Mono.just(code));
    }
}
