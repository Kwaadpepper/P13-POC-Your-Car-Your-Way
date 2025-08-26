package com.ycyw.users.application.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import org.eclipse.jdt.annotation.Nullable;

@Configuration
public class AppConfiguration {
  public static final long SERIAL_VERSION_UID = 1L;

  /** Expiration in minutes. */
  private static final Integer MIN_JWT_EXPIRATION = 1;

  private final String appName;
  private final String allowedOrigins;
  private final String jwtIssuer;
  private final String jwtSecretKey;
  private final String jwtCookieName;
  private final Integer jwtTokenExpiration;
  private final Integer jwtRefreshExpiration;
  private final String accessRevokedPrefix;
  private final String refreshStoragePrefix;

  AppConfiguration(
      @Value("${spring.application.name}") @Nullable final String appName,
      @Value("${server.cors.allowed-origins}") @Nullable final String allowedOrigins,
      @Value("${jwt.issuer}") @Nullable final String jwtIssuer,
      @Value("${jwt.secret_key}") @Nullable final String jwtSecretKey,
      @Value("${jwt.cookie.name}") @Nullable final String jwtCookieName,
      @Value("${jwt.token.expiration}") @Nullable final Integer jwtTokenExpiration,
      @Value("${jwt.refresh.expiration}") @Nullable final Integer jwtRefreshExpiration,
      @Value("${keystorage.revoked_token_prefix}") @Nullable final String accessRevokedPrefix,
      @Value("${keystorage.refresh_token_prefix}") @Nullable final String refreshStoragePrefix) {

    if (jwtSecretKey == null) {
      throw new IllegalStateException("Property 'jwt.secret_key' has to be set");
    }
    if (jwtSecretKey.length() < 64) {
      throw new IllegalStateException(
          "Property 'jwt.secret_key' must be at least 64 characters long");
    }

    if (jwtTokenExpiration == null) {
      throw new IllegalStateException("Property 'jwt.token.expiration' has to be set");
    }

    if (jwtTokenExpiration < MIN_JWT_EXPIRATION) {
      throw new IllegalStateException(
          "Property 'jwt.token.expiration' must be greater or equals to %d"
              .formatted(MIN_JWT_EXPIRATION));
    }

    if (jwtRefreshExpiration == null) {
      throw new IllegalStateException("Property 'jwt.refresh.expiration' has to be set");
    }

    if (jwtRefreshExpiration < MIN_JWT_EXPIRATION) {
      throw new IllegalStateException(
          "Property 'jwt.refresh.expiration' must be greater or equals to %d"
              .formatted(MIN_JWT_EXPIRATION));
    }

    if (jwtRefreshExpiration <= jwtTokenExpiration) {
      throw new IllegalStateException(
          "Property 'jwt.refresh.expiration' must be greater than 'jwt.token.expiration'");
    }

    this.appName = assertNotEmpty(appName, "spring.application.name");
    this.allowedOrigins = assertNotEmpty(allowedOrigins, "server.cors.allowed-origins");
    this.jwtIssuer = assertNotEmpty(jwtIssuer, "jwt.issuer");
    this.jwtSecretKey = assertNotEmpty(jwtSecretKey, "jwt.secret_key");
    this.jwtCookieName = assertNotEmpty(jwtCookieName, "jwt.cookie.name");

    this.jwtTokenExpiration = jwtTokenExpiration;
    this.jwtRefreshExpiration = jwtRefreshExpiration;

    this.accessRevokedPrefix =
        assertNotEmpty(accessRevokedPrefix, "keystorage.revoked_token_prefix");
    this.refreshStoragePrefix =
        assertNotEmpty(refreshStoragePrefix, "keystorage.refresh_token_prefix");
  }

  public String getAppName() {
    return appName;
  }

  public String getAllowedOrigins() {
    return allowedOrigins;
  }

  public String getJwtIssuer() {
    return jwtIssuer;
  }

  public String getJwtSecretKey() {
    return jwtSecretKey;
  }

  public String getJwtCookieName() {
    return jwtCookieName;
  }

  public Integer getJwtTokenExpiration() {
    return jwtTokenExpiration;
  }

  public Integer getJwtRefreshExpiration() {
    return jwtRefreshExpiration;
  }

  public String getJwtAccessRevokedStoragePrefix() {
    return accessRevokedPrefix;
  }

  public String getJwtRefreshStoragePrefix() {
    return refreshStoragePrefix;
  }

  private final String assertNotEmpty(@Nullable String value, String property) {
    if (value == null || value.isBlank()) {
      throw new IllegalStateException("Property '%s' cannot be null or empty".formatted(property));
    }
    return value;
  }
}
