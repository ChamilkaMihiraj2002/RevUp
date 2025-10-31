package com.revup.employee_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI employeeServiceAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Employee Service API")
                        .description("RESTful API for managing employee profiles, specializations, and availability")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("RevUp Support")
                                .email("support@revup.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")));
    }
}
