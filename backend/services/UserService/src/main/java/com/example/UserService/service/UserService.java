package com.example.UserService.service;

import com.example.UserService.dto.request.CreateUserRequest;
import com.example.UserService.dto.request.UpdateUserRequest;
import com.example.UserService.dto.UserDto;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

public interface UserService {
    Mono<UserDto> createUser(CreateUserRequest request);
    Mono<UserDto> getUserById(Long id);
    Flux<UserDto> getAllUsers();
    Mono<UserDto> updateUser(Long id, UpdateUserRequest request);
    Mono<Void> deleteUser(Long id);
}
