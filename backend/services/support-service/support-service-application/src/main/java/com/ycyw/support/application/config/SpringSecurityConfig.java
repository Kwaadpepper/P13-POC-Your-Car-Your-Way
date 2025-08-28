package com.ycyw.support.application.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.ycyw.support.application.component.JwtAuthenticationFilter;

import org.eclipse.jdt.annotation.Nullable;

@Configuration
public class SpringSecurityConfig {

  /** User Argon2 instead of BCrypt. */
  @Bean
  PasswordEncoder passwordEncoder() {
    return Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();
  }

  @Bean
  SecurityFilterChain securityFilterChain(
      HttpSecurity http,
      JwtAuthenticationFilter jwtAuthenticationFilter,
      AppConfiguration appConfiguration,
      PublicRoutesConfig publicRoutesConfig)
      throws Exception {

    final String appName = appConfiguration.getAppName();
    @Nullable final List<String> publicRoutesList = publicRoutesConfig.getPublicRoutes();

    if (publicRoutesList == null) {
      throw new IllegalStateException("Public routes cannot be null");
    }

    List<String> routesToIgnore =
        publicRoutesList.stream().map(route -> route.replaceAll("^/" + appName, "")).toList();

    jwtAuthenticationFilter.setIgnoreUrls(routesToIgnore);

    return http.sessionManagement(
            // No cookie session, just state less API.
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        // No CSRF for stateless APIs.
        .csrf(AbstractHttpConfigurer::disable)
        .exceptionHandling(
            handling ->
                handling.authenticationEntryPoint(
                    new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
        .authorizeHttpRequests(
            request -> {
              // Allow non protected AuthRequestToUrls are not protected.
              request.requestMatchers(routesToIgnore.toArray(new String[0])).permitAll();

              // Any other routes are.
              request.anyRequest().fullyAuthenticated();
            })
        // Filter requests to check JWT and assert it matches an actual user.
        .addFilterAt(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .build();
  }
}
