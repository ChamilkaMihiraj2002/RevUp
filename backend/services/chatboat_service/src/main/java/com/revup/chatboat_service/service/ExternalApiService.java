package com.revup.chatboat_service.service;

import com.revup.chatboat_service.exception.ExternalApiException;
import reactor.core.publisher.Mono;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;


import java.time.Duration;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;

@Service
public class ExternalApiService {
    private static final Logger log = LoggerFactory.getLogger(ExternalApiService.class);

    private final WebClient webClient;
    private final String path;
    private final Duration timeout;
    private static final ObjectMapper mapper = new ObjectMapper();

    public ExternalApiService(WebClient.Builder builder,
                              @Value("${external.api.base-url:http://localhost:8084}") String baseUrl,
                              @Value("${external.api.path:/api/service-types}") String path,
                              @Value("${external.api.timeout-seconds:5}") long timeoutSeconds) {
        this.webClient = builder.baseUrl(baseUrl).build();
        this.path = path;
        this.timeout = Duration.ofSeconds(timeoutSeconds);
    }

    /**
     * Fetch data from external microservice (reactive).
     */
    public Mono<String> fetchData(String query) {
        return fetchBody(query)
            .map(body -> {
                try {
                    return summarizeJson(body);
                } catch (JsonProcessingException e) {
                    log.warn("Failed to parse external API JSON, returning raw body. Error: {}", e.getMessage());
                    return body;
                }
            });
    }

    /**
     * Fetch the raw JSON body from the external API (no parsing, reactive).
     */
    public Mono<String> fetchRawJson(String query) {
        return fetchBody(query);
    }

    /**
     * Fetch the external API and parse the JSON into a list of ServiceType (reactive).
     */
    public Mono<List<ServiceType>> fetchServiceTypes(String query) {
        return fetchBody(query)
            .map(body -> {
                try {
                    return mapper.readValue(body, new TypeReference<List<ServiceType>>() {});
                } catch (JsonProcessingException e) {
                    throw new ExternalApiException("Failed to parse external API JSON: " + e.getMessage(), e);
                }
            });
    }

    /**
     * Summarize a JSON array of service types into a structured table for LLM prompts.
     */
    public String summarizeJson(String body) throws JsonProcessingException {
        List<ServiceType> services = mapper.readValue(body, new TypeReference<List<ServiceType>>() {});
        if (services.isEmpty()) {
            return "(no service types returned)";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Service Types Table:\n");
        sb.append("| Name         | Code         | Description         | Duration (min) | Price    |\n");
        sb.append("|--------------|--------------|---------------------|---------------|----------|\n");
        for (ServiceType s : services) {
            sb.append("| ");
            sb.append(s.getName() == null ? "N/A" : s.getName());
            sb.append(" | ");
            sb.append(s.getCode() == null ? "N/A" : s.getCode());
            sb.append(" | ");
            sb.append(s.getDescription() == null ? "N/A" : s.getDescription());
            sb.append(" | ");
            sb.append(s.getBaseDurationMinutes() == null ? "N/A" : s.getBaseDurationMinutes().toString());
            sb.append(" | ");
            sb.append(s.getBasePrice() == null ? "N/A" : s.getBasePrice().toString());
            sb.append(" |\n");
        }
        sb.append("\nUse this table to answer questions about service types, durations, and prices.\n");
        return sb.toString();
    }

    /**
     * Helper that performs the GET request and returns the response body as Mono<String> (reactive).
     */
    private Mono<String> fetchBody(String query) {
        String uri = path.startsWith("/") ? path : ("/" + path);
        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(String.class)
                .doOnNext(body -> System.out.println("[DEBUG] External API response body for uri=" + uri + ": " + body))
                .switchIfEmpty(Mono.error(new ExternalApiException("External API returned empty body for uri=" + uri)))
                .onErrorResume(WebClientResponseException.class, ex -> {
                    int status = ex.getStatusCode().value();
                    System.out.println("[ERROR] External API responded with error: status=" + status + ", body=" + ex.getResponseBodyAsString());
                    log.error("External API responded with error: status={}, body={}", status, ex.getResponseBodyAsString(), ex);
                    return Mono.error(new ExternalApiException("External API error: status=" + status, ex));
                })
                .onErrorResume(ExternalApiException.class, ex -> {
                    System.out.println("[ERROR] ExternalApiException: " + ex.getMessage());
                    return Mono.error(ex);
                })
                .onErrorResume(RuntimeException.class, ex -> {
                    System.out.println("[ERROR] Failed to call external API: uri=" + uri + ", error=" + ex.getMessage());
                    log.error("Failed to call external API: uri={}", uri, ex);
                    return Mono.error(new ExternalApiException("Failed to call external API: " + ex.getMessage(), ex));
                });
    }
}
