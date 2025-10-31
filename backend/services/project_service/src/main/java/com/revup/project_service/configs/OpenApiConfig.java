package com.revup.project_service.configs;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI projectServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Project Service API")
                        .description("RESTful API for managing projects and milestones in RevUp automobile service system")
                        .version("1.0.0"));
    }
}
