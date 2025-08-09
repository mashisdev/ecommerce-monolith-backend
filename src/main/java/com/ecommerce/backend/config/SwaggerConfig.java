package com.ecommerce.backend.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "E-commerce Project Backend API",
                version = "1.0.0",
                contact = @Contact(
                        name = "mashisdev",
                        url = "https://mashisdev.github.io/"
                )
        ),
        servers = @Server(
                description = "Development Server",
                url = "http://localhost:8080"
        )
)
@SecurityScheme(
        name = "bearerAuth",
        description = "JWT authentication using a Bearer token.",
        type = SecuritySchemeType.HTTP,
        in = SecuritySchemeIn.HEADER,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class SwaggerConfig {
}
