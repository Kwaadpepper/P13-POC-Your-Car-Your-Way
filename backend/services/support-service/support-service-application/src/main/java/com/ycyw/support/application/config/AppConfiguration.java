package com.ycyw.support.application.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import org.eclipse.jdt.annotation.Nullable;

@Configuration
public class AppConfiguration {
  public static final long SERIAL_VERSION_UID = 1L;

  private final String appName;
  private final String jwtCookieName;

  AppConfiguration(
      @Value("${spring.application.name}") @Nullable final String appName,
      @Value("${jwt.cookie.name}") @Nullable final String jwtCookieName) {

    this.appName = assertNotEmpty(appName, "spring.application.name");
    this.jwtCookieName = assertNotEmpty(jwtCookieName, "jwt.cookie.name");
  }

  public String getAppName() {
    return appName;
  }

  public String getJwtCookieName() {
    return jwtCookieName;
  }

  private final String assertNotEmpty(@Nullable String value, String property) {
    if (value == null || value.isBlank()) {
      throw new IllegalStateException("Property '%s' cannot be null or empty".formatted(property));
    }
    return value;
  }
}
