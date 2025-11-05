package com.example.UserService.service.Impl;

import com.example.UserService.dto.request.CreateUserRequest;
import com.example.UserService.dto.request.UpdateUserRequest;
import com.example.UserService.dto.UserDto;
import com.example.UserService.entity.User;
import com.example.UserService.exception.ResourceNotFoundException;
import com.example.UserService.mapper.UserMapper;
import com.example.UserService.repository.UserRepository;
import com.example.UserService.service.UserService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final Scheduler jdbcScheduler;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, Scheduler jdbcScheduler, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.jdbcScheduler = jdbcScheduler;
        this.userMapper = userMapper;
    }

    @Override
    public Mono<UserDto> createUser(CreateUserRequest request) {
        return Mono.fromCallable(() -> {
                    User user = userMapper.toEntity(request);
                    User savedUser = userRepository.save(user);
                    return userMapper.toDto(savedUser);
                })
                .subscribeOn(jdbcScheduler);
    }

    @Override
    public Mono<UserDto> getUserById(Long id) {
        return Mono.fromCallable(() -> userRepository.findById(id))
                .subscribeOn(jdbcScheduler)
                .flatMap(optionalUser -> optionalUser
                        .map(user -> Mono.just(userMapper.toDto(user)))
                        .orElseGet(() -> Mono.error(new ResourceNotFoundException("User not found with id: " + id))));
    }

    @Override
    public Flux<com.example.UserService.dto.UserDto> getAllUsers() {
        return Mono.fromCallable(() -> userRepository.findAll())
                .subscribeOn(jdbcScheduler)
                .flatMapMany(Flux::fromIterable)
                .map(userMapper::toDto);
    }

    @Override
    public Mono<UserDto> updateUser(Long id, UpdateUserRequest request) {
        return Mono.fromCallable(() -> {
                    User user = userRepository.findById(id)
                            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
                    userMapper.updateEntityFromDto(user, request);
                    User updatedUser = userRepository.save(user);
                    return userMapper.toDto(updatedUser);
                })
                .subscribeOn(jdbcScheduler);
    }

    @Override
    public Mono<Void> deleteUser(Long id) {
        return Mono.fromRunnable(() -> userRepository.deleteById(id))
                .subscribeOn(jdbcScheduler)
                .then();
    }
}
