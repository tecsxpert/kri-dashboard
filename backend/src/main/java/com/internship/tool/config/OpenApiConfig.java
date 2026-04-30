package com.internship.tool.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@OpenAPIDefinition
@SecurityScheme(
        name        = "bearerAuth",
        type        = SecuritySchemeType.HTTP,
        scheme      = "bearer",
        bearerFormat = "JWT",
        description = "Paste your JWT token (without 'Bearer ' prefix). Obtain it from POST /auth/login."
)
public class OpenApiConfig {

    @Bean
    public OpenAPI kriDashboardOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("KRI Dashboard API")
                        .version("v1")
                        .description("Backend REST APIs for the KRI Dashboard application. " +
                                     "Provides endpoints for managing KRI records, " +
                                     "file attachments, and authentication.")
                        .contact(new Contact()
                                .name("KRI Dashboard Team")
                                .email("support@kridashboard.internal")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Local development server")
                ));
    }
}
