package com.revup.employee_service.repository;

import com.revup.employee_service.model.AvailabilityStatus;
import com.revup.employee_service.model.Employee;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface EmployeeRepository extends R2dbcRepository<Employee, Long> {

    Mono<Employee> findByUserId(Long userId);

    Mono<Employee> findByEmployeeCode(String employeeCode);

    Mono<Boolean> existsByUserId(Long userId);

    Mono<Boolean> existsByEmployeeCode(String employeeCode);

    Flux<Employee> findByAvailabilityStatus(AvailabilityStatus status);

    Flux<Employee> findBySpecializationContainingIgnoreCase(String specialization);

    @Query("SELECT * FROM employee_schema.employees WHERE average_rating >= :minRating ORDER BY average_rating DESC")
    Flux<Employee> findTopRatedEmployees(Double minRating);

    @Query("SELECT * FROM employee_schema.employees WHERE total_services_completed >= :minServices ORDER BY total_services_completed DESC")
    Flux<Employee> findMostExperiencedEmployees(Integer minServices);
}
