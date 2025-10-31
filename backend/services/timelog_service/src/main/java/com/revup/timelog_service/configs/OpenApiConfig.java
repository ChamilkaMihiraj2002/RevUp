package com.revup.timelog_service.configs;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI timelogServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Time Logging Service API")
                        .description("RESTful API for time logging, workload tracking, and productivity reports in RevUp automobile service system")
                        .version("1.0.0"));
    }
}
