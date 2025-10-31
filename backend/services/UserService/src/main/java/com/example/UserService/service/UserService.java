package com.example.UserService.service;

import com.example.UserService.dto.request.CreateUserRequest;
import com.example.UserService.dto.request.UpdateUserRequest;
import com.example.UserService.dto.response.UserResponse;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

public interface UserService {
    Mono<UserResponse> createUser(CreateUserRequest request);
    Mono<UserResponse> getUserById(Long id);
    Flux<UserResponse> getAllUsers();
    Mono<UserResponse> updateUser(Long id, UpdateUserRequest request);
    Mono<Void> deleteUser(Long id);
}
