package com.ycyw.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.WebFilter;

import com.ycyw.gateway.component.JwtPresenceFilter;

@Configuration
public class SecurityConfig {
  @Bean
  WebFilter jwtPresenceFilter(AppConfiguration appConfiguration) {
    return new JwtPresenceFilter(appConfiguration);
  }
}
