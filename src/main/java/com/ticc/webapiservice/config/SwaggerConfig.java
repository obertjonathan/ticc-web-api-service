package com.ticc.webapiservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
@SecurityScheme(name = "bearerToken",type = SecuritySchemeType.HTTP,scheme = "bearer",bearerFormat = "JWT")
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI()
    {
        return new OpenAPI()
        .info(new Info().title("TICC - Web API Documentation")
        .description("TICC Documentation for Web API Service").version("1.0"));
    }
}
