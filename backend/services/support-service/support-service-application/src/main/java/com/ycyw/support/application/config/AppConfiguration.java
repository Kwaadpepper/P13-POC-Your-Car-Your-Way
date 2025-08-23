package com.ycyw.support.application.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import org.eclipse.jdt.annotation.Nullable;

@Configuration
public class AppConfiguration {
  public static final long SERIAL_VERSION_UID = 1L;

  private final String appName;

  AppConfiguration(@Value("${spring.application.name}") @Nullable final String appName) {

    this.appName = assertNotEmpty(appName, "spring.application.name");
  }

  public String getAppName() {
    return appName;
  }

  private final String assertNotEmpty(@Nullable String value, String property) {
    if (value == null || value.isBlank()) {
      throw new IllegalStateException("Property '%s' cannot be null or empty".formatted(property));
    }
    return value;
  }
}
