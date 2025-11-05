package com.revup.vehicleservice.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


// service-to-service REST communication

@Slf4j
@Component
public class UserServiceClient {

    private final WebClient userServiceWebClient;

    public UserServiceClient(@Qualifier("userServiceWebClient") WebClient userServiceWebClient) {
        this.userServiceWebClient = userServiceWebClient;
    }


 //Check if a user exists in User Service

    public Mono<Boolean> userExists(Long userId) {
        log.debug("Checking if user exists with id: {}", userId);
        return userServiceWebClient
                .get()
                .uri("/api/v1/users/{userId}/exists", userId)
                .retrieve()
                .bodyToMono(Boolean.class)
                .doOnSuccess(exists -> log.debug("User {} exists: {}", userId, exists))
                .onErrorResume(error -> {
                    log.error("Error checking if user exists: {}", error.getMessage());
                    return Mono.just(false);
                });
    }


    public Mono<Void> addVehicleToUser(Long userId, Long vehicleId) {
        log.info("Notifying User Service to add vehicle {} to user {}", vehicleId, userId);
        return userServiceWebClient
                .post()
                .uri("/api/v1/users/{userId}/vehicles/{vehicleId}", userId, vehicleId)
                .retrieve()
                .bodyToMono(Void.class)
                .doOnSuccess(v -> log.info("Successfully notified User Service about vehicle addition"))
                .onErrorResume(error -> {
                    log.error("Failed to notify User Service about vehicle addition: {}", error.getMessage());
              
                    return Mono.empty();
                });
    }


    public Mono<Void> removeVehicleFromUser(Long userId, Long vehicleId) {
        log.info("Notifying User Service to remove vehicle {} from user {}", vehicleId, userId);
        return userServiceWebClient
                .delete()
                .uri("/api/v1/users/{userId}/vehicles/{vehicleId}", userId, vehicleId)
                .retrieve()
                .bodyToMono(Void.class)
                .doOnSuccess(v -> log.info("Successfully notified User Service about vehicle removal"))
                .onErrorResume(error -> {
                    log.error("Failed to notify User Service about vehicle removal: {}", error.getMessage());
                    return Mono.empty();
                });
    }
}
