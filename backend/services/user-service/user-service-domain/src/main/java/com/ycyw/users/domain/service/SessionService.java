package com.ycyw.users.domain.service;

import com.ycyw.shared.ddd.objectvalues.JwtAccessToken;
import com.ycyw.users.domain.model.entity.credential.CredentialId;
import com.ycyw.users.domain.model.valueobject.TokenPair;
import com.ycyw.users.domain.model.valueobject.jwt.AccessTokenClaims;
import com.ycyw.users.domain.model.valueobject.jwt.AccessTokenSubject;
import com.ycyw.users.domain.model.valueobject.jwt.JwtRefreshToken;
import com.ycyw.users.domain.model.valueobject.jwt.RefreshTokenClaims;
import com.ycyw.users.domain.model.valueobject.jwt.RefreshTokenSubject;
import com.ycyw.users.domain.port.service.token.JwtAccessTokenManager;
import com.ycyw.users.domain.port.service.token.JwtRefreshTokenManager;
import com.ycyw.users.domain.port.service.token.TokenManager;

import org.eclipse.jdt.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SessionService {
  private static final Logger logger = LoggerFactory.getLogger(SessionService.class);
  private static final String ACCESS_TOKEN_IS_INVALID = "Access token is invalid: %s";
  private static final String ACCESS_TOKEN_COULD_NOT_BE_EXTRACTED =
      "Access token could not be extracted";
  private final JwtAccessTokenManager accessTokenManager;
  private final JwtRefreshTokenManager refreshTokenManager;

  public SessionService(
      JwtAccessTokenManager accessTokenManager, JwtRefreshTokenManager refreshTokenManager) {
    this.accessTokenManager = accessTokenManager;
    this.refreshTokenManager = refreshTokenManager;
  }

  public TokenPair generateSessionFor(AuthenticableUser userAccount) {
    final var identifier = userAccount.identifier();
    final var role = userAccount.role();

    final var accessTokenClaims = newAccessTokenClaims(identifier, role);
    final var refreshTokenClaims = newRefreshTokenClaims(identifier);

    final var accessToken = accessTokenManager.generate(accessTokenClaims);
    final var refreshToken = refreshTokenManager.generate(refreshTokenClaims);

    return new TokenPair(accessToken, refreshToken);
  }

  public @Nullable AccessTokenSubject verify(TokenPair tokenPair) {
    try {
      final var accessToken = tokenPair.accessToken();
      final var refreshToken = tokenPair.refreshToken();

      if (accessTokenManager.validate(accessToken)
          instanceof TokenManager.TokenValidity.Invalid(var reason)) {
        throw new SessionServiceException(
            ACCESS_TOKEN_IS_INVALID.formatted(reason));
      }

      if (refreshTokenManager.validate(refreshToken)
          instanceof TokenManager.TokenValidity.Invalid(var reason)) {
        throw new SessionServiceException(
            ACCESS_TOKEN_IS_INVALID.formatted(reason));
      }

      @Nullable AccessTokenClaims accessTokenClaims = accessTokenManager.extract(accessToken);
      if (accessTokenClaims == null) {
        throw new SessionServiceException(ACCESS_TOKEN_COULD_NOT_BE_EXTRACTED);
      }

      @Nullable RefreshTokenClaims refreshTokenClaims = refreshTokenManager.extract(refreshToken);
      if (refreshTokenClaims == null) {
        throw new SessionServiceException("Refresh token could not be extracted");
      }

      if (!accessTokenClaims.subject().value().equals(refreshTokenClaims.subject().value())) {
        logger.debug("Access token and refresh token claims do not match");
        return null;
      }

      return accessTokenClaims.subject();
    } catch (SessionServiceException e) {
      logger.debug(e.getMessage());
      return null;
    }
  }

  public @Nullable TokenPair refresh(TokenPair tokenPair) {
    try {
      if (verify(tokenPair) == null) {
        throw new SessionServiceException("Verification failed during refresh");
      }

      JwtAccessToken oldAccessToken = tokenPair.accessToken();
      JwtRefreshToken refreshToken = tokenPair.refreshToken();

      @Nullable AccessTokenClaims accessTokenClaims = accessTokenManager.extract(oldAccessToken);
      if (accessTokenClaims == null) {
        throw new SessionServiceException(ACCESS_TOKEN_COULD_NOT_BE_EXTRACTED);
      }

      JwtAccessToken newAccessToken = accessTokenManager.generate(accessTokenClaims);
      accessTokenManager.invalidate(oldAccessToken);

      return new TokenPair(newAccessToken, refreshToken);
    } catch (SessionServiceException e) {
      logger.debug(e.getMessage());
      return null;
    }
  }

  public void invalidate(TokenPair tokenPair) {
    final var accessToken = tokenPair.accessToken();
    final var refreshToken = tokenPair.refreshToken();
    accessTokenManager.invalidate(accessToken);
    refreshTokenManager.invalidate(refreshToken);
  }

  public @Nullable JwtRefreshToken findRefreshTokenFor(JwtAccessToken accessToken) {
    try {
      if (accessTokenManager.validate(accessToken)
          instanceof TokenManager.TokenValidity.Invalid(var reason)) {
        throw new SessionServiceException(
            ACCESS_TOKEN_IS_INVALID.formatted(reason));
      }

      @Nullable AccessTokenClaims accessTokenClaims = accessTokenManager.extract(accessToken);
      if (accessTokenClaims == null) {
        throw new SessionServiceException(ACCESS_TOKEN_COULD_NOT_BE_EXTRACTED);
      }

      final var accessTokenSubject = accessTokenClaims.subject();
      final var refreshTokenSubject = new RefreshTokenSubject(accessTokenSubject.value());
      final var refreshToken = refreshTokenManager.find(refreshTokenSubject);
      if (refreshToken == null) {
        throw new SessionServiceException(
            "No refresh token found for subject: " + accessTokenSubject.value());
      }

      return refreshToken;
    } catch (SessionServiceException e) {
      logger.debug(e.getMessage());
      return null;
    }
  }

  private AccessTokenClaims newAccessTokenClaims(CredentialId identifier, String role) {
    return new AccessTokenClaims(new AccessTokenSubject(identifier.value()), role);
  }

  private RefreshTokenClaims newRefreshTokenClaims(CredentialId identifier) {
    return new RefreshTokenClaims(new RefreshTokenSubject(identifier.value()));
  }

  public record AuthenticableUser(CredentialId identifier, String role) {}

  public static class SessionServiceException extends RuntimeException {
    public SessionServiceException(String message) {
      super(message);
    }
  }
}
