package com.artichourey.ecommerce.productservice.config;

import java.util.List;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI productServiceAPI() {

        SecurityScheme securityScheme = new SecurityScheme()
                .name("Authorization") // 🔥 IMPORTANT (not bearerAuth)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT");

        return new OpenAPI()
                .info(new Info()
                        .title("User Service API")
                        .description("Product And Category management APIs for E-commerce Platform")
                        .version("1.0"))

                // 🔥 THIS FIXES YOUR MAIN ISSUE (VERY IMPORTANT)
                .servers(List.of(new Server().url("/")))

                .components(new Components()
                        .addSecuritySchemes("bearerAuth", securityScheme))

                .addSecurityItem(new SecurityRequirement()
                        .addList("bearerAuth"));
    }
}

