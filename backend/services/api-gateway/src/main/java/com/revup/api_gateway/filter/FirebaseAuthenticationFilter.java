package com.revup.api_gateway.filter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.revup.api_gateway.dto.Role;
import com.revup.api_gateway.dto.UserDto;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Firebase Authentication Filter
 * Validates Firebase JWT tokens and enforces role-based access control
 */
@Slf4j
@Component
public class FirebaseAuthenticationFilter implements Filter {

    private final WebClient.Builder webClientBuilder;
    
    @Value("${user.service.url:http://localhost:8083}")
    private String userServiceUrl;

    public FirebaseAuthenticationFilter(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    // Public endpoints that don't require authentication
    private static final List<String> PUBLIC_ENDPOINTS = Arrays.asList(
        "/gateway/health",
        "/gateway/info",
        "/gateway/services",
        "/actuator/health",
        "/actuator/info",
        "/api/v1/users",
        "/api/v1/service-types",
        "/api/v1/appointment-services",
        "/api/v1/appointments"
    );

    // Endpoints that require TECHNICIAN role
    private static final List<String> TECHNICIAN_ONLY_ENDPOINTS = Arrays.asList(
        // "/api/v1/service-types",
        // "/api/v1/appointment-services"
        // Add more technician-only endpoints here
    );

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String path = httpRequest.getRequestURI();
        log.debug("Processing request: {} {}", httpRequest.getMethod(), path);

        // Skip authentication for public endpoints
        if (isPublicEndpoint(path)) {
            log.debug("Public endpoint, skipping authentication: {}", path);
            chain.doFilter(request, response);
            return;
        }

        // Handle OPTIONS requests (CORS preflight)
        if ("OPTIONS".equalsIgnoreCase(httpRequest.getMethod())) {
            chain.doFilter(request, response);
            return;
        }

        try {
            // 1. Extract JWT token from Authorization header
            String token = extractToken(httpRequest);

            if (token == null || token.trim().isEmpty()) {
                log.warn("Missing or empty Authorization header for path: {}", path);
                sendUnauthorized(httpResponse, "Missing Authorization header");
                return;
            }

            // 2. Verify Firebase token
            FirebaseToken decodedToken;
            try {
                decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
            } catch (FirebaseAuthException e) {
                log.warn("Invalid Firebase token: {}", e.getMessage());
                sendUnauthorized(httpResponse, "Invalid or expired token");
                return;
            }

            String firebaseUID = decodedToken.getUid();
            log.debug("Firebase UID: {}", firebaseUID);

            // 3. Fetch user from User Service
            UserDto user = getUserByFirebaseUID(firebaseUID);

            if (user == null) {
                log.warn("User not found in database for Firebase UID: {}", firebaseUID);
                sendUnauthorized(httpResponse, "User not found in database");
                return;
            }

            log.debug("User found: {} (Role: {})", user.getUserId(), user.getRole());

            // 4. Check role-based permissions
            if (!hasPermission(path, user.getRole())) {
                log.warn("User {} (Role: {}) attempted to access restricted endpoint: {}", 
                    user.getUserId(), user.getRole(), path);
                sendForbidden(httpResponse, "Insufficient permissions. This endpoint requires TECHNICIAN role.");
                return;
            }

            // 5. Add user info to request headers for downstream services
            UserContextRequest wrappedRequest = new UserContextRequest(
                httpRequest, user.getUserId(), user.getRole(), firebaseUID
            );

            log.info("Authentication successful for user: {} accessing: {}", user.getUserId(), path);
            chain.doFilter(wrappedRequest, response);

        } catch (Exception e) {
            log.error("Authentication error for path {}: {}", path, e.getMessage(), e);
            sendError(httpResponse, "Authentication error: " + e.getMessage());
        }
    }

    /**
     * Extract JWT token from Authorization header
     */
    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    /**
     * Fetch user details from User Service by Firebase UID
     */
    private UserDto getUserByFirebaseUID(String firebaseUID) {
        try {
            WebClient webClient = webClientBuilder.build();
            String url = userServiceUrl + "/api/v1/users/firebase/" + firebaseUID;
            
            log.debug("Calling User Service: {}", url);
            
            UserDto user = webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(UserDto.class)
                .block(); // Blocking call - consider making async in production
            
            return user;
        } catch (Exception e) {
            log.error("Failed to fetch user from User Service for Firebase UID {}: {}", 
                firebaseUID, e.getMessage());
            return null;
        }
    }

    /**
     * Check if endpoint is public (no authentication required)
     */
    private boolean isPublicEndpoint(String path) {
        return PUBLIC_ENDPOINTS.stream()
            .anyMatch(path::startsWith);
    }

    /**
     * Check if user has permission to access the endpoint
     */
    private boolean hasPermission(String path, Role role) {
        // Check if endpoint requires TECHNICIAN role
        boolean requiresTechnician = TECHNICIAN_ONLY_ENDPOINTS.stream()
            .anyMatch(path::startsWith);

        if (requiresTechnician) {
            return role == Role.TECHNICIAN;
        }

        // All other authenticated endpoints are accessible to any authenticated user
        return true;
    }

    /**
     * Send 401 Unauthorized response
     */
    private void sendUnauthorized(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write(
            String.format("{\"error\": \"Unauthorized\", \"message\": \"%s\"}", message)
        );
    }

    /**
     * Send 403 Forbidden response
     */
    private void sendForbidden(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        response.getWriter().write(
            String.format("{\"error\": \"Forbidden\", \"message\": \"%s\"}", message)
        );
    }

    /**
     * Send 500 Internal Server Error response
     */
    private void sendError(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        response.setContentType("application/json");
        response.getWriter().write(
            String.format("{\"error\": \"Server Error\", \"message\": \"%s\"}", message)
        );
    }
}
