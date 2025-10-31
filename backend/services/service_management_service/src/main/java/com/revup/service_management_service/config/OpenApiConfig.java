package com.revup.service_management_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI serviceManagementAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Service Management API")
                        .description("RESTful API for service catalog and instance management with real-time progress tracking")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("RevUp Support")
                                .email("support@revup.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")));
    }
}
