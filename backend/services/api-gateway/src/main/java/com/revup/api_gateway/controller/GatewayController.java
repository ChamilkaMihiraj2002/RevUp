package com.revup.api_gateway.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

/**
 * Gateway Controller for health checks and service discovery information
 */
@RestController
@RequestMapping("/gateway")
public class GatewayController {

    @Autowired
    private DiscoveryClient discoveryClient;

    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "API Gateway");
        return ResponseEntity.ok(response);
    }

    /**
     * Get all registered services
     */
    @GetMapping("/services")
    public ResponseEntity<List<String>> getServices() {
        List<String> services = discoveryClient.getServices();
        return ResponseEntity.ok(services);
    }

    /**
     * Get instances of a specific service
     */
    @GetMapping("/services/{serviceName}")
    public ResponseEntity<List<ServiceInstance>> getServiceInstances(@PathVariable String serviceName) {
        List<ServiceInstance> instances = discoveryClient.getInstances(serviceName);
        return ResponseEntity.ok(instances);
    }

    /**
     * Get gateway information
     */
    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getGatewayInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("name", "RevUp API Gateway");
        info.put("version", "1.0.0");
        info.put("description", "API Gateway for RevUp microservices");
        
        Map<String, String> routes = new HashMap<>();
        routes.put("/api/users/**", "User Service");
        routes.put("/api/vehicles/**", "Vehicle Service");
        info.put("routes", routes);
        
        return ResponseEntity.ok(info);
    }
}
