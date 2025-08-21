package com.ycyw.users.application.service;

import java.util.Optional;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.web.util.WebUtils;

import com.ycyw.shared.ddd.objectvalues.JwtAccessToken;
import com.ycyw.users.application.config.AppConfiguration;
import com.ycyw.users.domain.model.valueobject.jwt.JwtRefreshToken;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.eclipse.jdt.annotation.Nullable;

@Service
public class CookieService {
  private final String jwtCookieName;
  private final String jwtRefreshCookieName;

  private static final String JWT_COOKIE_NAME_SUFFIX = "-jwt";
  private static final String JWT_REFRESH_COOKIE_NAME_SUFFIX = "-refresh";

  private final Integer jwtCookieExpiration;
  private final Integer jwtRefreshCookieExpiration;

  private static final String JWT_COOKIE_HTTP_PATH = "/";
  private static final String JWT_REFRESH_COOKIE_HTTP_PATH = "/";

  CookieService(final AppConfiguration appConfiguration) {
    this.jwtCookieName = appConfiguration.getJwtCookieName();
    this.jwtRefreshCookieName = appConfiguration.getJwtCookieName();
    this.jwtCookieExpiration = appConfiguration.getJwtTokenExpiration();
    this.jwtRefreshCookieExpiration = appConfiguration.getJwtRefreshExpiration();
  }

  public ResponseCookie generateJwtCookie(final JwtAccessToken jwtToken) {
    return generateCookie(
        jwtCookieName + JWT_COOKIE_NAME_SUFFIX,
        jwtToken.value(),
        JWT_COOKIE_HTTP_PATH,
        jwtCookieExpiration);
  }

  public ResponseCookie generateCookieRemoval() {
    return generateCookie(jwtCookieName + JWT_COOKIE_NAME_SUFFIX, null, JWT_COOKIE_HTTP_PATH);
  }

  public ResponseCookie generateRefreshJwtCookie(final JwtRefreshToken jwtRefreshToken) {
    return generateCookie(
        jwtCookieName + JWT_REFRESH_COOKIE_NAME_SUFFIX,
        jwtRefreshToken.value(),
        JWT_REFRESH_COOKIE_HTTP_PATH,
        jwtRefreshCookieExpiration);
  }

  public ResponseCookie generateRefreshJwtCookieRemoval() {
    return generateCookie(
        jwtRefreshCookieName + JWT_REFRESH_COOKIE_NAME_SUFFIX, null, JWT_REFRESH_COOKIE_HTTP_PATH);
  }

  public Optional<JwtAccessToken> getJwtAccessTokenFromRequest(final HttpServletRequest request) {
    return getCookieValueByName(request, "%s-jwt".formatted(jwtCookieName))
        .map(Cookie::getValue)
        .filter(cookieValue -> cookieValue != null && !cookieValue.isBlank())
        .map(JwtAccessToken::new);
  }

  public Optional<JwtRefreshToken> getRefreshTokenUuidFromRequest(
      final HttpServletRequest request) {
    return getCookieValueByName(request, "%s-refresh".formatted(jwtCookieName))
        .map(Cookie::getValue)
        .filter(cookieValue -> cookieValue != null && !cookieValue.isBlank())
        .map(JwtRefreshToken::new);
  }

  private ResponseCookie generateCookie(String name, @Nullable String value, String path) {
    return generateCookie(name, value, path, 0);
  }

  private ResponseCookie generateCookie(
      String name, @Nullable String value, String path, Integer expirationInMinutes) {
    return ResponseCookie.from(name)
        .value(value)
        .path(path)
        .maxAge(expirationInMinutes * 60L)
        .httpOnly(true)
        .build();
  }

  private Optional<Cookie> getCookieValueByName(HttpServletRequest request, String name) {
    return Optional.ofNullable(WebUtils.getCookie(request, name));
  }
}
