package com.ycyw.support.application.service;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.ycyw.shared.ddd.exceptions.DomainConstraintException;
import com.ycyw.shared.ddd.objectvalues.JwtAccessToken;
import com.ycyw.support.application.config.AppConfiguration;
import com.ycyw.support.application.exception.exceptions.ServerErrorException;
import com.ycyw.support.application.security.AuthenticatedUser;
import com.ycyw.support.infrastructure.storage.KeyStorage;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jdt.annotation.Nullable;

@Component
public class AuthenticationService {
  private static final Logger logger = LogManager.getLogger(AuthenticationService.class);

  private final String jwtIssuer;
  private final SecretKey jwtSigningKey;

  private final KeyStorage keyStorage;
  private final String revokedAccessPrefix;

  public AuthenticationService(AppConfiguration appConfiguration, KeyStorage keyStorage) {
    this.jwtIssuer = appConfiguration.getJwtIssuer();
    this.jwtSigningKey = keyFromString(appConfiguration.getJwtSecretKey());
    this.keyStorage = keyStorage;
    this.revokedAccessPrefix = appConfiguration.getAccessStoragePrefix();
  }

  public @Nullable AuthenticatedUser getAuthenticated() {
    var securityContext = SecurityContextHolder.getContext();
    @Nullable Authentication authentication = securityContext.getAuthentication();

    if (authentication == null) {
      return null;
    }

    return toUser(authentication);
  }

  public AuthenticatedUser authenticate(final JwtAccessToken accessToken)
      throws BadCredentialsException {
    try {
      if (keyStorage.retrieve(revokedAccessPrefix + accessToken.value()) != null) {
        throw new BadCredentialsException("Token has been revoked");
      }

      final var claims = extractAllClaims(accessToken);
      final var userId = UUID.fromString(claims.getSubject());
      @Nullable final String role = claims.get("role", String.class);

      if (role == null) {
        throw new BadCredentialsException("Invalid token claims");
      }

      return new AuthenticatedUser(userId, accessToken, role);
    } catch (ExpiredJwtException e) {
      throw new BadCredentialsException("Token has expired");

    } catch (DomainConstraintException e) {
      throw new BadCredentialsException("Account cannot be used for the moment");
    }
  }

  private AuthenticatedUser toUser(final Authentication authentication)
      throws ServerErrorException {
    final var principal = authentication.getPrincipal();

    if (principal instanceof AuthenticatedUser authenticatedUser) {
      return authenticatedUser;
    }

    logger.debug("Given authentication principal is not a AuthenticatedUser instance.");
    throw new ServerErrorException(
        "Expected principal to be a '%s' instance given is '%s'"
            .formatted(AuthenticatedUser.class, principal.getClass()));
  }

  private Claims extractAllClaims(JwtAccessToken jwtAccessToken) {
    return Jwts.parser()
        .verifyWith(jwtSigningKey)
        .requireIssuer(jwtIssuer)
        .build()
        .parseSignedClaims(jwtAccessToken.value())
        .getPayload();
  }

  private SecretKey keyFromString(String base64String) {
    return Keys.hmacShaKeyFor(base64String.getBytes(StandardCharsets.UTF_8));
  }
}
