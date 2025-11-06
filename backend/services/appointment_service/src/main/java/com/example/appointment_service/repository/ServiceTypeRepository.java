package com.example.appointment_service.repository;

import com.example.appointment_service.entity.ServiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceTypeRepository extends JpaRepository<ServiceType, Long> {
    boolean existsByCode(String code);
}
