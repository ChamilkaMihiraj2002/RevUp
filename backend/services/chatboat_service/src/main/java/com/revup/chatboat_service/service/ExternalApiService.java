package com.revup.chatboat_service.service;

import com.revup.chatboat_service.exception.ExternalApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

@Service
public class ExternalApiService {
    private static final Logger log = LoggerFactory.getLogger(ExternalApiService.class);

    private final WebClient webClient;
    private final String path;
    private final Duration timeout;

    public ExternalApiService(WebClient.Builder builder,
                              @Value("${external.api.base-url:http://localhost:8084}") String baseUrl,
                              @Value("${external.api.path:/api/v1/data}") String path,
                              @Value("${external.api.timeout-seconds:5}") long timeoutSeconds) {
        this.webClient = builder.baseUrl(baseUrl).build();
        this.path = path;
        this.timeout = Duration.ofSeconds(timeoutSeconds);
    }

    /**
     * Fetch data from external microservice. Throws {@link ExternalApiException} on error.
     */
    public String fetchData(String query) {
        String encoded = URLEncoder.encode(query == null ? "" : query, StandardCharsets.UTF_8);
        String uri = path + "?q=" + encoded;
        try {
            String body = webClient.get()
                    .uri(uri)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block(timeout);
            if (body == null) {
                throw new ExternalApiException("External API returned empty body for uri=" + uri);
            }
            return body;
        } catch (WebClientResponseException ex) {
            int status = ex.getStatusCode().value();
            log.error("External API responded with error: status={}, body={}", status, ex.getResponseBodyAsString(), ex);
            throw new ExternalApiException("External API error: status=" + status, ex);
        } catch (ExternalApiException ex) {
            throw ex;
        } catch (RuntimeException ex) {
            log.error("Failed to call external API: uri={}", uri, ex);
            throw new ExternalApiException("Failed to call external API: " + ex.getMessage(), ex);
        }
    }
}
