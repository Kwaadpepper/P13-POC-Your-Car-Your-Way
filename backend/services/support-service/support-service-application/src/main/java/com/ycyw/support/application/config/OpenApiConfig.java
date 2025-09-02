package com.ycyw.support.application.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenApiConfig {

  @Bean
  OpenAPI openApi() {
    return new OpenAPI()
        .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
        .components(
            new Components().addSecuritySchemes("Bearer Authentication", createApiKeyScheme()))
        .info(
            new Info()
                .title("Support Service REST API")
                .description("Support Service Api written as an OpenClassRoom project.")
                .version("0.0.1")
                .contact(
                    new Contact()
                        .name("Munsch Jeremy")
                        .email("github@jeremydev.ovh")
                        .url("https://jeremydev.ovh")));
  }

  private SecurityScheme createApiKeyScheme() {
    return new SecurityScheme().type(SecurityScheme.Type.HTTP).bearerFormat("JWT").scheme("bearer");
  }
}
