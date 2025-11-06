package com.revup.vehicleservice.repository;

import com.revup.vehicleservice.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    /**
     * Find all vehicles belonging to a specific user
     */
    List<Vehicle> findByUserId(Long userId);
    
    /**
     * Find vehicle by registration number
     */
    Optional<Vehicle> findByRegistrationNo(String registrationNo);
    
    /**
     * Check if a vehicle exists with the given registration number
     */
    boolean existsByRegistrationNo(String registrationNo);
    
    /**
     * Count vehicles for a specific user
     */
    @Query("SELECT COUNT(v) FROM Vehicle v WHERE v.userId = :userId")
    long countByUserId(@Param("userId") Long userId);
}
