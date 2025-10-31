package com.revup.user_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {
    
    @Bean
    public OpenAPI userServiceOpenAPI() {
        Server devServer = new Server();
        devServer.setUrl("http://localhost:8083");
        devServer.setDescription("Development server");
        
        Contact contact = new Contact();
        contact.setName("RevUp Support");
        contact.setEmail("support@revup.com");
        
        Info info = new Info()
                .title("User Management Service API")
                .version("1.0.0")
                .description("API for managing user profiles and roles in RevUp Automobile Service System")
                .contact(contact);
        
        return new OpenAPI()
                .info(info)
                .servers(List.of(devServer));
    }
}
