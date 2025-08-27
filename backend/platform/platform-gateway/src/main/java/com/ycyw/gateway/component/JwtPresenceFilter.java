package com.ycyw.gateway.component;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import com.ycyw.gateway.config.AppConfiguration;

import org.eclipse.jdt.annotation.Nullable;
import reactor.core.publisher.Mono;

public class JwtPresenceFilter implements WebFilter {
  private final String cookieName;
  private final List<String> publicRoutes;
  private final AntPathMatcher pathMatcher = new AntPathMatcher();
  private final CorsConfigurationSource corsConfigurationSource;

  public JwtPresenceFilter(
      AppConfiguration appConfiguration, CorsConfigurationSource corsConfigurationSource) {
    this.cookieName = appConfiguration.getJwtCookieName() + "-jwt";
    this.publicRoutes = appConfiguration.getPublicRouteList();
    this.corsConfigurationSource = corsConfigurationSource;
  }

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
    String requestPath = exchange.getRequest().getPath().value();

    if (isPublicRoute(requestPath)) {
      return chain.filter(exchange);
    }

    final var cookies = exchange.getRequest().getCookies().get(cookieName);

    if (cookies == null || cookies.isEmpty()) {
      return setUnauthorizedResponse(exchange);
    }

    final var jwtValue = cookies.getFirst().getValue();

    if (!isValidJwt(jwtValue)) {
      return setUnauthorizedResponse(exchange);
    }

    return chain.filter(exchange);
  }

  private Mono<Void> setUnauthorizedResponse(ServerWebExchange exchange) {
    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);

    // Récupérer la configuration CORS à partir de la source pour la requête actuelle
    @Nullable final CorsConfiguration corsConfiguration =
        this.corsConfigurationSource.getCorsConfiguration(exchange);

    if (corsConfiguration != null) {
      HttpHeaders headers = exchange.getResponse().getHeaders();

      // Appliquer manuellement les en-têtes CORS
      if (!corsConfiguration.getAllowedOrigins().isEmpty()) {
        headers.set(
            "Access-Control-Allow-Origin",
            String.join(", ", corsConfiguration.getAllowedOrigins()));
      }

      if (!corsConfiguration.getAllowedMethods().isEmpty()) {
        // Convertir la liste d'énumérations en liste de chaînes de caractères
        String methods =
            corsConfiguration.getAllowedMethods().stream()
                .map(String::valueOf)
                .collect(Collectors.joining(", "));
        headers.set("Access-Control-Allow-Methods", methods);
      }

      if (!corsConfiguration.getAllowedHeaders().isEmpty()) {
        headers.set(
            "Access-Control-Allow-Headers",
            String.join(", ", corsConfiguration.getAllowedHeaders()));
      }

      if (corsConfiguration.getAllowCredentials() != null) {
        headers.set(
            "Access-Control-Allow-Credentials",
            String.valueOf(corsConfiguration.getAllowCredentials()));
      }
    }

    return exchange.getResponse().setComplete();
  }

  private boolean isPublicRoute(String path) {
    return publicRoutes.stream().anyMatch(pattern -> pathMatcher.match(pattern, path));
  }

  private boolean isValidJwt(String token) {
    String jwtRegex = "^[A-Za-z0-9-_]+\\.[A-Za-z0-9-_]+\\.[A-Za-z0-9-_]+$";
    return Pattern.matches(jwtRegex, token);
  }
}
