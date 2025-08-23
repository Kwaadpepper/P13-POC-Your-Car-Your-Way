package com.ycyw.users.application.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.ycyw.users.application.component.JwtAuthenticationFilter;

@Configuration
public class SpringSecurityConfig {

  /** User Argon2 instead of BCrypt. */
  @Bean
  PasswordEncoder passwordEncoder() {
    return Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();
  }

  @Bean
  SecurityFilterChain securityFilterChain(
      final HttpSecurity http, final JwtAuthenticationFilter jwtAuthenticationFilter)
      throws Exception {

    final var allowNonAuthRequestToUrls =
        new String[] {
          // Swagger
          "/v3/api-docs/**",
          "/swagger-ui.html",
          "/swagger-ui/**",
          // Health check
          "/actuator/**",
          // Auth routes
          "/auth/login",
          "/auth/register",
          "/auth/refresh-token",
          "/auth/logout"
        };
    jwtAuthenticationFilter.setIgnoreUrls(List.of(allowNonAuthRequestToUrls));

    return http.sessionManagement(
            // No cookie session, just state less API.
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        // No CSRF for stateless APIs.
        .csrf(AbstractHttpConfigurer::disable)
        .cors(Customizer.withDefaults())
        .exceptionHandling(
            handling ->
                handling.authenticationEntryPoint(
                    new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
        .authorizeHttpRequests(
            request -> {
              // Allow non protected AuthRequestToUrls are not protected.
              request.requestMatchers(allowNonAuthRequestToUrls).permitAll();

              // Any other routes are.
              request.anyRequest().fullyAuthenticated();
            })
        // Filter requests to check JWT and assert it matches an actual user.
        .addFilterAt(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .build();
  }
}
