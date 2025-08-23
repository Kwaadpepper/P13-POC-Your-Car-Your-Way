package com.ycyw.gateway.component;

import java.util.List;
import java.util.regex.Pattern;

import org.springframework.http.HttpStatus;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import com.ycyw.gateway.config.AppConfiguration;

import reactor.core.publisher.Mono;

/**
 * Filtre qui vérifie simplement la présence et la forme du JWT dans le cookie. Ne valide pas la
 * signature !
 */
public class JwtPresenceFilter implements WebFilter {
  private final String cookieName;
  private final List<String> publicRoutes;
  private final AntPathMatcher pathMatcher = new AntPathMatcher();

  public JwtPresenceFilter(AppConfiguration appConfiguration) {
    this.cookieName = appConfiguration.getJwtCookieName() + "-jwt";
    this.publicRoutes = appConfiguration.getPublicRouteList();
  }

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
    String requestPath = exchange.getRequest().getPath().value();

    // Si la route est publique, laisse passer
    if (isPublicRoute(requestPath)) {
      return chain.filter(exchange);
    }

    final var cookies = exchange.getRequest().getCookies().get(cookieName);

    if (cookies == null || cookies.isEmpty()) {
      exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
      return exchange.getResponse().setComplete();
    }

    final var jwtValue = cookies.getFirst().getValue();

    if (!isValidJwt(jwtValue)) {
      exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
      return exchange.getResponse().setComplete();
    }

    return chain.filter(exchange);
  }

  private boolean isPublicRoute(String path) {
    // Test les patterns et routes exactes
    return publicRoutes.stream().anyMatch(pattern -> pathMatcher.match(pattern, path));
  }

  private boolean isValidJwt(String token) {
    String jwtRegex = "^[A-Za-z0-9-_]+\\.[A-Za-z0-9-_]+\\.[A-Za-z0-9-_]+$";
    return Pattern.matches(jwtRegex, token);
  }
}
