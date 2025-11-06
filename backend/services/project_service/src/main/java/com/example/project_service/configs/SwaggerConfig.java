package com.example.project_service.configs;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Value("${server.port:8085}")
    private String serverPort;

    @Bean
    public OpenAPI projectServiceOpenAPI() {
        Server server = new Server();
        server.setUrl("http://localhost:" + serverPort);
        server.setDescription("Development Server");

        Contact contact = new Contact();
        contact.setEmail("revup@example.com");
        contact.setName("RevUp Team");
        contact.setUrl("https://github.com/ChamilkaMihiraj2002/RevUp");

        License license = new License()
                .name("MIT License")
                .url("https://opensource.org/licenses/MIT");

        Info info = new Info()
                .title("RevUp Project Service API")
                .version("1.0.0")
                .contact(contact)
                .description("This API provides CRUD operations for managing projects in the RevUp system. " +
                        "Projects can be created, updated, retrieved, and deleted. " +
                        "You can also filter projects by user, status, or both.")
                .termsOfService("https://revup.example.com/terms")
                .license(license);

        return new OpenAPI()
                .info(info)
                .servers(List.of(server));
    }
}
