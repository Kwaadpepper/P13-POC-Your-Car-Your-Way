package com.ycyw.gateway.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import org.eclipse.jdt.annotation.Nullable;

@Configuration
public class AppConfiguration {
  private final List<String> publicRouteList;
  private final String jwtCookieName;

  AppConfiguration(
      @Value("${jwt.cookie.name}") @Nullable final String jwtCookieName,
      PublicRoutesConfig publicRoutes) {

    @Nullable final List<String> routeList = publicRoutes.getPublicRoutes();

    if (routeList == null || routeList.isEmpty()) {
      throw new IllegalStateException("Property 'gateway.public-routes' cannot be null or empty");
    }

    this.jwtCookieName = assertNotEmpty(jwtCookieName, "jwt.cookie.name");
    this.publicRouteList = routeList;
  }

  public String getJwtCookieName() {
    return jwtCookieName;
  }

  public List<String> getPublicRouteList() {
    return publicRouteList;
  }

  private final String assertNotEmpty(@Nullable String value, String property) {
    if (value == null || value.isBlank()) {
      throw new IllegalStateException("Property '%s' cannot be null or empty".formatted(property));
    }
    return value;
  }
}
