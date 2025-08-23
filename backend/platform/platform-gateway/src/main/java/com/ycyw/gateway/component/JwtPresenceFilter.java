package com.ycyw.gateway.component;

import java.util.regex.Pattern;

import org.springframework.http.HttpStatus;
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

  public JwtPresenceFilter(AppConfiguration appConfiguration) {
    this.cookieName = appConfiguration.getJwtCookieName() + "-jwt";
  }

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
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

  private boolean isValidJwt(String token) {
    String jwtRegex = "^[A-Za-z0-9-_]+\\.[A-Za-z0-9-_]+\\.[A-Za-z0-9-_]+$";
    return Pattern.matches(jwtRegex, token);
  }
}
