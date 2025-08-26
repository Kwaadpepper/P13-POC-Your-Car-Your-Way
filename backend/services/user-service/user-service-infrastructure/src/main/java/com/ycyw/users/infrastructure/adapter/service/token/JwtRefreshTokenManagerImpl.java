package com.ycyw.users.infrastructure.adapter.service.token;

import java.util.Collections;
import java.util.UUID;

import com.ycyw.users.domain.model.valueobject.jwt.JwtRefreshToken;
import com.ycyw.users.domain.model.valueobject.jwt.RefreshTokenClaims;
import com.ycyw.users.domain.model.valueobject.jwt.RefreshTokenSubject;
import com.ycyw.users.domain.port.service.token.JwtRefreshTokenManager;
import com.ycyw.users.domain.port.service.token.JwtTokenProcessor;
import com.ycyw.users.domain.port.service.token.JwtTokenProcessor.TokenClaims;
import com.ycyw.users.infrastructure.storage.KeyStorage;

import org.eclipse.jdt.annotation.Nullable;

public class JwtRefreshTokenManagerImpl implements JwtRefreshTokenManager {
  private final JwtTokenProcessor jwtTokenProcessor;
  private final KeyStorage keyStorage;
  private final String refreshPrefix;

  public JwtRefreshTokenManagerImpl(
      JwtTokenProcessor jwtTokenProcessor, KeyStorage keyStorage, String refreshPrefix) {
    this.jwtTokenProcessor = jwtTokenProcessor;
    this.keyStorage = keyStorage;
    this.refreshPrefix = refreshPrefix;
  }

  @Override
  public @Nullable JwtRefreshToken find(RefreshTokenSubject subject) {
    final var key = refreshKey(subject);
    @Nullable final String stored = keyStorage.retrieve(key);

    return (stored == null || stored.isEmpty()) ? null : new JwtRefreshToken(stored);
  }

  @Override
  public JwtRefreshToken generate(RefreshTokenClaims claims) {
    final var subject = claims.subject();
    final var additional = Collections.singletonMap("role", claims.role());
    final var token = jwtTokenProcessor.generateJwtToken(subject.value().toString(), additional);
    final var jwtRefreshToken = new JwtRefreshToken(token.value());

    keyStorage.store(refreshKey(subject), jwtRefreshToken.value());

    return jwtRefreshToken;
  }

  @Override
  public TokenValidity validate(JwtRefreshToken token) {
    final var jwtToken = toJwtToken(token);

    if (isRevoked(token)) {
      return new TokenValidity.Invalid(TokenInvalidityReason.REVOKED);
    }

    if (jwtTokenProcessor.hasTokenExpired(jwtToken)) {
      return new TokenValidity.Invalid(TokenInvalidityReason.EXPIRED);
    }

    return jwtTokenProcessor.extractApiToken(jwtToken) != null
        ? new TokenValidity.Valid()
        : new TokenValidity.Invalid(TokenInvalidityReason.UNKNOWN);
  }

  @Override
  public @Nullable RefreshTokenClaims extract(JwtRefreshToken token) {
    @Nullable final TokenClaims claims = jwtTokenProcessor.extractApiToken(toJwtToken(token));

    if (claims == null) {
      return null;
    }

    @Nullable String role = claims.additional().get("role");

    if (role == null) {
      return null;
    }

    return new RefreshTokenClaims(new RefreshTokenSubject(UUID.fromString(claims.subject())), role);
  }

  @Override
  public void invalidate(JwtRefreshToken token) {
    @Nullable TokenClaims claims = jwtTokenProcessor.extractApiToken(toJwtToken(token));

    if (claims != null && !claims.subject().isEmpty()) {
      String subject = claims.subject();
      keyStorage.forget(refreshKey(new RefreshTokenSubject(UUID.fromString(subject))));
    } else {
      // Si l'extraction a échoué, on parcourt toutes les clés pour supprimer les tokens
      // correspondants
      keyStorage
          .listKeys(refreshPrefix + "*")
          .forEach(
              key -> {
                @Nullable String stored = keyStorage.retrieve(key);
                if (token.value().equals(stored)) {
                  keyStorage.forget(key);
                }
              });
    }
  }

  private boolean isRevoked(JwtRefreshToken token) {
    @Nullable final TokenClaims claims = jwtTokenProcessor.extractApiToken(toJwtToken(token));

    if (claims == null || claims.subject().isEmpty()) {
      // Révoqué si on ne peut pas extraire le subject
      return true;
    }

    final var refreshSubject = new RefreshTokenSubject(UUID.fromString(claims.subject()));

    @Nullable final String stored = keyStorage.retrieve(refreshKey(refreshSubject));

    return stored == null || !stored.equals(token.value());
  }

  private String refreshKey(RefreshTokenSubject subject) {
    return refreshPrefix + subject.value().toString();
  }

  private static JwtTokenProcessor.JwtToken toJwtToken(JwtRefreshToken token) {
    return new JwtTokenProcessor.JwtToken(token.value());
  }
}
