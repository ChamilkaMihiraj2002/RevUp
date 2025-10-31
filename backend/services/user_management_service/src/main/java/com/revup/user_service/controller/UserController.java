package com.revup.user_service.controller;

import com.revup.user_service.model.dto.CreateUserRequest;
import com.revup.user_service.model.dto.UpdateUserRequest;
import com.revup.user_service.model.dto.UserResponse;
import com.revup.user_service.model.enums.UserRole;
import com.revup.user_service.model.enums.UserStatus;
import com.revup.user_service.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "APIs for managing users and their profiles")
public class UserController {
    
    private final UserService userService;
    
    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Create a new user account linked to Firebase UID")
    public Mono<ResponseEntity<UserResponse>> createUser(@Valid @RequestBody CreateUserRequest request) {
        log.info("Received request to create user with Firebase UID: {}", request.getFirebaseUid());
        return userService.createUser(request)
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response));
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Retrieve user details by user ID")
    public Mono<ResponseEntity<UserResponse>> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok);
    }
    
    @GetMapping("/firebase/{firebaseUid}")
    @Operation(summary = "Get user by Firebase UID", description = "Retrieve user details by Firebase UID")
    public Mono<ResponseEntity<UserResponse>> getUserByFirebaseUid(@PathVariable String firebaseUid) {
        return userService.getUserByFirebaseUid(firebaseUid)
                .map(ResponseEntity::ok);
    }
    
    @GetMapping("/email/{email}")
    @Operation(summary = "Get user by email", description = "Retrieve user details by email address")
    public Mono<ResponseEntity<UserResponse>> getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email)
                .map(ResponseEntity::ok);
    }
    
    @GetMapping
    @Operation(summary = "Get all users", description = "Retrieve all users")
    public Flux<UserResponse> getAllUsers() {
        return userService.getAllUsers();
    }
    
    @GetMapping("/role/{role}")
    @Operation(summary = "Get users by role", description = "Retrieve users by their role")
    public Flux<UserResponse> getUsersByRole(@PathVariable UserRole role) {
        return userService.getUsersByRole(role);
    }
    
    @GetMapping("/status/{status}")
    @Operation(summary = "Get users by status", description = "Retrieve users by their status")
    public Flux<UserResponse> getUsersByStatus(@PathVariable UserStatus status) {
        return userService.getUsersByStatus(status);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update user", description = "Update user profile information")
    public Mono<ResponseEntity<UserResponse>> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request) {
        return userService.updateUser(id, request)
                .map(ResponseEntity::ok);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user", description = "Soft delete a user (marks as DELETED)")
    public Mono<ResponseEntity<Void>> deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()));
    }
    
    @GetMapping("/exists/firebase/{firebaseUid}")
    @Operation(summary = "Check if user exists", description = "Check if a user exists with the given Firebase UID")
    public Mono<ResponseEntity<Boolean>> userExists(@PathVariable String firebaseUid) {
        return userService.userExists(firebaseUid)
                .map(ResponseEntity::ok);
    }
}
