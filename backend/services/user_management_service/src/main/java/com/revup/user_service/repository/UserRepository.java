package com.revup.user_service.repository;

import com.revup.user_service.model.entity.User;
import com.revup.user_service.model.enums.UserRole;
import com.revup.user_service.model.enums.UserStatus;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends R2dbcRepository<User, Long> {
    
    Mono<User> findByFirebaseUid(String firebaseUid);
    
    Mono<User> findByEmail(String email);
    
    Mono<Boolean> existsByFirebaseUid(String firebaseUid);
    
    Mono<Boolean> existsByEmail(String email);
    
    Flux<User> findByRole(UserRole role);
    
    Flux<User> findByStatus(UserStatus status);
    
    @Query("SELECT * FROM user_schema.users WHERE role = :role AND status = :status")
    Flux<User> findByRoleAndStatus(UserRole role, UserStatus status);
}
