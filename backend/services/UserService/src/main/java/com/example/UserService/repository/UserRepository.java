package com.example.UserService.repository;

import com.example.UserService.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    
    /**
     * Fetch user with vehicles to avoid N+1 query problem
     */
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.vehicles WHERE u.userId = :id")
    Optional<User> findByIdWithVehicles(@Param("id") Long id);
    
    /**
     * Fetch all users with their vehicles in a single query
     */
    @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.vehicles")
    List<User> findAllWithVehicles();
}
