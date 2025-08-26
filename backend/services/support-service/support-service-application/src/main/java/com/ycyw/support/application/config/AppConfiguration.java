package com.ycyw.support.application.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import org.eclipse.jdt.annotation.Nullable;

@Configuration
public class AppConfiguration {
  public static final long SERIAL_VERSION_UID = 1L;

  private final String appName;
  private final String jwtIssuer;
  private final String jwtCookieName;
  private final String jwtSecretKey;

  private final String accessStoragePrefix;

  AppConfiguration(
      @Value("${spring.application.name}") @Nullable final String appName,
      @Value("${jwt.issuer}") @Nullable final String jwtIssuer,
      @Value("${jwt.cookie.name}") @Nullable final String jwtCookieName,
      @Value("${jwt.secret_key}") @Nullable final String jwtSecretKey,
      @Value("${keystorage.revoked_token_prefix}") @Nullable final String accessRevokedPrefix) {

    if (jwtSecretKey == null) {
      throw new IllegalStateException("Property 'jwt.secret_key' has to be set");
    }
    if (jwtSecretKey.length() < 64) {
      throw new IllegalStateException(
          "Property 'jwt.secret_key' must be at least 64 characters long");
    }

    this.appName = assertNotEmpty(appName, "spring.application.name");
    this.jwtIssuer = assertNotEmpty(jwtIssuer, "jwt.issuer");
    this.jwtCookieName = assertNotEmpty(jwtCookieName, "jwt.cookie.name");
    this.jwtSecretKey = assertNotEmpty(jwtSecretKey, "jwt.secret_key");
    this.accessStoragePrefix =
        assertNotEmpty(accessRevokedPrefix, "keystorage.revoked_token_prefix");
  }

  public String getAppName() {
    return appName;
  }

  public String getJwtIssuer() {
    return jwtIssuer;
  }

  public String getJwtCookieName() {
    return jwtCookieName;
  }

  public String getJwtSecretKey() {
    return jwtSecretKey;
  }

  public String getAccessStoragePrefix() {
    return accessStoragePrefix;
  }

  private final String assertNotEmpty(@Nullable String value, String property) {
    if (value == null || value.isBlank()) {
      throw new IllegalStateException("Property '%s' cannot be null or empty".formatted(property));
    }
    return value;
  }
}
