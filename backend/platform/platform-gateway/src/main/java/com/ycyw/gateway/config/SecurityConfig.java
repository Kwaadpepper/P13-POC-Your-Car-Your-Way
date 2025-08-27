package com.ycyw.gateway.config;

import org.springframework.cloud.gateway.config.GlobalCorsProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.server.WebFilter;

import com.ycyw.gateway.component.JwtPresenceFilter;

@Configuration
public class SecurityConfig {
  @Bean
  WebFilter jwtPresenceFilter(
      AppConfiguration appConfiguration, CorsConfigurationSource corsConfigurationSource) {
    return new JwtPresenceFilter(appConfiguration, corsConfigurationSource);
  }

  /**
   * Crée un bean CorsConfigurationSource en utilisant la configuration CORS existante de la
   * Gateway.
   */
  @Bean
  CorsConfigurationSource corsConfigurationSource(GlobalCorsProperties globalCorsProperties) {
    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

    // Récupère la configuration pour le chemin "/**" qui est déjà définie dans le YAML
    CorsConfiguration corsConfiguration = globalCorsProperties.getCorsConfigurations().get("/**");

    if (corsConfiguration != null) {
      source.registerCorsConfiguration("/**", corsConfiguration);
    }

    return source;
  }
}
