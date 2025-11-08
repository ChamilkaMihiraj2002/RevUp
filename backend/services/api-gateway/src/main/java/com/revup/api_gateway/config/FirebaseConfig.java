package com.revup.api_gateway.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Firebase Configuration
 * Initializes Firebase Admin SDK for JWT token validation
 */
@Slf4j
@Configuration
public class FirebaseConfig {

    @Value("${firebase.credentials.path:#{null}}")
    private String credentialsPath;

    @PostConstruct
    public void initialize() {
        try {
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseOptions options;
                
                if (credentialsPath != null && !credentialsPath.isEmpty()) {
                    // Option 1: Load from file path (for local development)
                    log.info("Initializing Firebase with credentials file: {}", credentialsPath);
                    InputStream serviceAccount = new FileInputStream(credentialsPath);
                    options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .build();
                } else {
                    // Option 2: Load from classpath (service-account.json in resources)
                    log.info("Initializing Firebase with classpath credentials");
                    InputStream serviceAccount = new ClassPathResource("service-account.json").getInputStream();
                    options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .build();
                }
                
                FirebaseApp.initializeApp(options);
                log.info("Firebase Admin SDK initialized successfully");
            }
        } catch (IOException e) {
            log.error("Failed to initialize Firebase Admin SDK", e);
            log.warn("API Gateway will run but Firebase authentication will not work!");
            log.warn("Please add Firebase service account JSON file:");
            log.warn("  1. Download from Firebase Console > Project Settings > Service Accounts");
            log.warn("  2. Place at: src/main/resources/service-account.json");
            log.warn("  OR set: firebase.credentials.path in application.yaml");
        }
    }
}
