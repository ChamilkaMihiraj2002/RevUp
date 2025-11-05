package com.example.UserService.controller;

import com.example.UserService.dto.request.CreateUserRequest;
import com.example.UserService.dto.request.UpdateUserRequest;
import com.example.UserService.dto.UserDto;
import com.example.UserService.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public Mono<ResponseEntity<UserDto>> createUser(@Valid @RequestBody CreateUserRequest request) {
        log.info("Creating new user with email: {}", request.getEmail());
        return userService.createUser(request)
                .map(user -> new ResponseEntity<>(user, HttpStatus.CREATED))
                .doOnSuccess(user -> log.info("User created successfully with id: {}", user.getBody().getUserId()));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<UserDto>> getUser(@PathVariable Long id) {
        log.info("Fetching user with id: {}", id);
        return userService.getUserById(id)
                .map(ResponseEntity::ok);
    }

    @GetMapping
    public Flux<UserDto> getAllUsers() {
        log.info("Fetching all users");
        return userService.getAllUsers();
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<UserDto>> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request) {
        log.info("Updating user with id: {}", id);
        return userService.updateUser(id, request)
                .map(ResponseEntity::ok)
                .doOnSuccess(user -> log.info("User updated successfully with id: {}", id));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteUser(@PathVariable Long id) {
        log.info("Deleting user with id: {}", id);
        return userService.deleteUser(id)
                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)))
                .doOnSuccess(response -> log.info("User deleted successfully with id: {}", id));
    }

    // Internal endpoints for service-to-service communication
    @PostMapping("/{userId}/vehicles/{vehicleId}")
    public Mono<ResponseEntity<Void>> addVehicleToUser(
            @PathVariable Long userId,
            @PathVariable Long vehicleId) {
        log.info("Adding vehicle {} to user {}", vehicleId, userId);
        return userService.addVehicleToUser(userId, vehicleId)
                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK)))
                .doOnSuccess(response -> log.info("Vehicle {} added to user {}", vehicleId, userId));
    }

    @DeleteMapping("/{userId}/vehicles/{vehicleId}")
    public Mono<ResponseEntity<Void>> removeVehicleFromUser(
            @PathVariable Long userId,
            @PathVariable Long vehicleId) {
        log.info("Removing vehicle {} from user {}", vehicleId, userId);
        return userService.removeVehicleFromUser(userId, vehicleId)
                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)))
                .doOnSuccess(response -> log.info("Vehicle {} removed from user {}", vehicleId, userId));
    }

    @GetMapping("/{userId}/exists")
    public Mono<ResponseEntity<Boolean>> checkUserExists(@PathVariable Long userId) {
        log.info("Checking if user exists with id: {}", userId);
        return userService.userExists(userId)
                .map(ResponseEntity::ok);
    }
}
