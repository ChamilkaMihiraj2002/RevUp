package com.example.UserService.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


@Slf4j
@Component
public class VehicleServiceClient {

    private final WebClient vehicleServiceWebClient;

    public VehicleServiceClient(WebClient.Builder webClientBuilder,
                                @Value("${vehicle.service.url:http://localhost:8082}") String vehicleServiceUrl) {
        this.vehicleServiceWebClient = webClientBuilder
                .baseUrl(vehicleServiceUrl)
                .build();
    }

    public Mono<Void> deleteVehiclesByUserId(Long userId) {
        log.info("Requesting deletion of all vehicles for user {}", userId);
        return vehicleServiceWebClient
                .delete()
                .uri("/api/v1/vehicles/user/{userId}", userId)
                .retrieve()
                .bodyToMono(Void.class)
                .doOnSuccess(v -> log.info("Successfully deleted vehicles for user {}", userId))
                .onErrorResume(error -> {
                    log.error("Failed to delete vehicles for user {}: {}", userId, error.getMessage());
           
                    return Mono.empty();
                });
    }
}
