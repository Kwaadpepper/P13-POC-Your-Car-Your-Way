package com.ycyw.users.infrastructure.adapter.service.token;

import java.util.Collections;
import java.util.UUID;

import com.ycyw.shared.ddd.objectvalues.JwtAccessToken;
import com.ycyw.users.domain.model.valueobject.jwt.AccessTokenClaims;
import com.ycyw.users.domain.model.valueobject.jwt.AccessTokenSubject;
import com.ycyw.users.domain.port.service.token.JwtAccessTokenManager;
import com.ycyw.users.domain.port.service.token.JwtTokenProcessor;
import com.ycyw.users.domain.port.service.token.JwtTokenProcessor.TokenClaims;
import com.ycyw.users.domain.port.service.token.TokenManager;
import com.ycyw.users.infrastructure.storage.KeyStorage;

import org.eclipse.jdt.annotation.Nullable;

public class JwtAccessTokenManagerImpl implements JwtAccessTokenManager {

  private final JwtTokenProcessor jwtTokenProcessor;
  private final KeyStorage keyStorage;
  private final String revokedAccessPrefix;

  public JwtAccessTokenManagerImpl(
      JwtTokenProcessor jwtTokenProcessor, KeyStorage keyStorage, String revokedAccessPrefix) {
    this.jwtTokenProcessor = jwtTokenProcessor;
    this.keyStorage = keyStorage;
    this.revokedAccessPrefix = revokedAccessPrefix;
  }

  @Override
  public JwtAccessToken generate(AccessTokenClaims claims) {
    final var subject = claims.subject();
    final var additional = Collections.singletonMap("role", claims.role());
    final var token = jwtTokenProcessor.generateJwtToken(subject.value().toString(), additional);

    return new JwtAccessToken(token.value());
  }

  @Override
  public TokenManager.TokenValidity validate(JwtAccessToken token) {
    final var jwtToken = toJwtToken(token);

    if (isRevoked(token)) {
      return new TokenManager.TokenValidity.Invalid(TokenManager.TokenInvalidityReason.REVOKED);
    }

    if (jwtTokenProcessor.hasTokenExpired(jwtToken)) {
      return new TokenManager.TokenValidity.Invalid(TokenManager.TokenInvalidityReason.EXPIRED);
    }
    @Nullable TokenClaims result = jwtTokenProcessor.extractApiToken(jwtToken);

    if (result != null) {
      return new TokenManager.TokenValidity.Valid();
    } else {
      return new TokenManager.TokenValidity.Invalid(TokenManager.TokenInvalidityReason.UNKNOWN);
    }
  }

  @Override
  public @Nullable AccessTokenClaims extract(JwtAccessToken token) {
    @Nullable final TokenClaims claims = jwtTokenProcessor.extractApiToken(toJwtToken(token));

    if (claims == null) {
      return null;
    }

    @Nullable String role = claims.additional().get("role");

    if (role == null) {
      return null;
    }

    return new AccessTokenClaims(new AccessTokenSubject(UUID.fromString(claims.subject())), role);
  }

  @Override
  public void invalidate(JwtAccessToken token) {
    @Nullable final TokenClaims claims = jwtTokenProcessor.extractApiToken(toJwtToken(token));

    final @Nullable Long expiry = (claims == null) ? null : claims.exp();

    if (expiry != null) {
      keyStorage.store(revokedKey(token), "1", expiry);
    } else {
      keyStorage.store(revokedKey(token), "1");
    }
  }

  private boolean isRevoked(JwtAccessToken token) {
    final @Nullable String revoked = keyStorage.retrieve(revokedKey(token));

    return (revoked != null && !revoked.isEmpty());
  }

  private String revokedKey(JwtAccessToken token) {
    return revokedAccessPrefix + token.value();
  }

  private static JwtTokenProcessor.JwtToken toJwtToken(JwtAccessToken token) {
    return new JwtTokenProcessor.JwtToken(token.value());
  }
}
