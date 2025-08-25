package com.ycyw.support.application.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.util.WebUtils;

import com.ycyw.shared.ddd.objectvalues.JwtAccessToken;
import com.ycyw.support.application.config.AppConfiguration;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.eclipse.jdt.annotation.Nullable;

@Component
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

  public @Nullable JwtAccessToken getJwtAccessTokenFromHttpRequest(
      final ServerHttpRequest request) {
    return getCookieValueByName(request, "%s-jwt".formatted(jwtCookieName))
        .map(Cookie::getValue)
        .filter(cookieValue -> cookieValue != null && !cookieValue.isBlank())
        .map(JwtAccessToken::new)
        .orElse(null);
  }

  private Optional<Cookie> getCookieValueByName(HttpServletRequest request, String name) {
    return Optional.ofNullable(WebUtils.getCookie(request, name));
  }

  private Optional<Cookie> getCookieValueByName(ServerHttpRequest request, String name) {
    return Optional.ofNullable(getCookie(request, name));
  }

  /**
   * Extrait un cookie d'une requête ServerHttpRequest en analysant l'en-tête "Cookie".
   *
   * @param request La requête HTTP.
   * @param name Le nom du cookie à rechercher.
   * @return Le Cookie s'il est trouvé, sinon null.
   */
  public static @Nullable Cookie getCookie(ServerHttpRequest request, String name) {
    Assert.notNull(request, "Request must not be null");
    Assert.notNull(name, "Name must not be null");

    final HttpHeaders headers = request.getHeaders();
    final List<String> cookieHeaders = headers.get("Cookie");

    // Gère le cas où l'en-tête "Cookie" n'est pas du tout présent
    if (cookieHeaders == null) {
      return null;
    }

    // La logique de stream parcourt tous les cookies de l'en-tête pour trouver le bon
    return cookieHeaders.stream()
        .flatMap(header -> Arrays.stream(header.split(";\\s*")))
        .map(cookieStr -> cookieStr.split("=", 2))
        .filter(parts -> parts.length == 2 && name.equals(parts[0]))
        .findFirst()
        .map(parts -> new Cookie(parts[0], parts[1]))
        .orElse(null);
  }
}
