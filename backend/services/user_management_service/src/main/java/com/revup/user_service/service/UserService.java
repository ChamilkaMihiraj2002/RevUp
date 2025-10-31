package com.revup.user_service.service;

import com.revup.user_service.exception.ResourceNotFoundException;
import com.revup.user_service.exception.DuplicateResourceException;
import com.revup.user_service.mapper.UserMapper;
import com.revup.user_service.model.dto.CreateUserRequest;
import com.revup.user_service.model.dto.UpdateUserRequest;
import com.revup.user_service.model.dto.UserResponse;
import com.revup.user_service.model.entity.User;
import com.revup.user_service.model.enums.UserRole;
import com.revup.user_service.model.enums.UserStatus;
import com.revup.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    
    @Transactional
    public Mono<UserResponse> createUser(CreateUserRequest request) {
        log.info("Creating user with Firebase UID: {}", request.getFirebaseUid());
        
        return userRepository.existsByFirebaseUid(request.getFirebaseUid())
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new DuplicateResourceException(
                                "User with Firebase UID " + request.getFirebaseUid() + " already exists"));
                    }
                    
                    User user = userMapper.toEntity(request);
                    user.setStatus(UserStatus.ACTIVE);
                    user.setCreatedAt(LocalDateTime.now());
                    user.setUpdatedAt(LocalDateTime.now());
                    
                    return userRepository.save(user);
                })
                .map(userMapper::toResponse)
                .doOnSuccess(response -> log.info("User created successfully with ID: {}", response.getId()))
                .doOnError(error -> log.error("Error creating user: {}", error.getMessage()));
    }
    
    public Mono<UserResponse> getUserById(Long id) {
        log.debug("Getting user by ID: {}", id);
        return userRepository.findById(id)
                .map(userMapper::toResponse)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("User not found with ID: " + id)));
    }
    
    public Mono<UserResponse> getUserByFirebaseUid(String firebaseUid) {
        log.debug("Getting user by Firebase UID: {}", firebaseUid);
        return userRepository.findByFirebaseUid(firebaseUid)
                .map(userMapper::toResponse)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException(
                        "User not found with Firebase UID: " + firebaseUid)));
    }
    
    public Mono<UserResponse> getUserByEmail(String email) {
        log.debug("Getting user by email: {}", email);
        return userRepository.findByEmail(email)
                .map(userMapper::toResponse)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("User not found with email: " + email)));
    }
    
    public Flux<UserResponse> getAllUsers() {
        log.debug("Getting all users");
        return userRepository.findAll()
                .map(userMapper::toResponse);
    }
    
    public Flux<UserResponse> getUsersByRole(UserRole role) {
        log.debug("Getting users by role: {}", role);
        return userRepository.findByRole(role)
                .map(userMapper::toResponse);
    }
    
    public Flux<UserResponse> getUsersByStatus(UserStatus status) {
        log.debug("Getting users by status: {}", status);
        return userRepository.findByStatus(status)
                .map(userMapper::toResponse);
    }
    
    @Transactional
    public Mono<UserResponse> updateUser(Long id, UpdateUserRequest request) {
        log.info("Updating user with ID: {}", id);
        
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("User not found with ID: " + id)))
                .flatMap(user -> {
                    userMapper.updateEntityFromRequest(request, user);
                    user.setUpdatedAt(LocalDateTime.now());
                    return userRepository.save(user);
                })
                .map(userMapper::toResponse)
                .doOnSuccess(response -> log.info("User updated successfully: {}", id))
                .doOnError(error -> log.error("Error updating user: {}", error.getMessage()));
    }
    
    @Transactional
    public Mono<Void> deleteUser(Long id) {
        log.info("Deleting user with ID: {}", id);
        
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("User not found with ID: " + id)))
                .flatMap(user -> {
                    user.setStatus(UserStatus.DELETED);
                    user.setUpdatedAt(LocalDateTime.now());
                    return userRepository.save(user);
                })
                .then()
                .doOnSuccess(v -> log.info("User deleted successfully: {}", id))
                .doOnError(error -> log.error("Error deleting user: {}", error.getMessage()));
    }
    
    public Mono<Boolean> userExists(String firebaseUid) {
        return userRepository.existsByFirebaseUid(firebaseUid);
    }
}
