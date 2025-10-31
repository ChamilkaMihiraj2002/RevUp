package com.revup.vehicle_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    
    @Bean
    public OpenAPI vehicleServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Vehicle Service API")
                        .version("1.0.0")
                        .description("Vehicle management for RevUp Automobile Service System"));
    }
}
