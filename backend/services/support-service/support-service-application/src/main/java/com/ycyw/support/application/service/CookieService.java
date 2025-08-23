package com.ycyw.support.application.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.util.WebUtils;

import com.ycyw.shared.ddd.objectvalues.JwtAccessToken;
import com.ycyw.support.application.config.AppConfiguration;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.eclipse.jdt.annotation.Nullable;

@Service
public class CookieService {
  private final String jwtCookieName;

  CookieService(final AppConfiguration appConfiguration) {
    this.jwtCookieName = appConfiguration.getJwtCookieName();
  }

  public @Nullable JwtAccessToken getJwtAccessTokenFromRequest(final HttpServletRequest request) {
    return getCookieValueByName(request, "%s-jwt".formatted(jwtCookieName))
        .map(Cookie::getValue)
        .filter(cookieValue -> cookieValue != null && !cookieValue.isBlank())
        .map(JwtAccessToken::new)
        .orElse(null);
  }

  private Optional<Cookie> getCookieValueByName(HttpServletRequest request, String name) {
    return Optional.ofNullable(WebUtils.getCookie(request, name));
  }
}
