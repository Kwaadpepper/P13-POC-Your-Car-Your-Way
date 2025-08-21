package com.ycyw.users.infrastructure.adapter.service.token;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

import com.ycyw.shared.ddd.objectvalues.JwtAccessToken;
import com.ycyw.shared.utils.UuidV7;
import com.ycyw.users.domain.model.valueobject.jwt.AccessTokenClaims;
import com.ycyw.users.domain.model.valueobject.jwt.AccessTokenSubject;
import com.ycyw.users.domain.port.service.token.JwtAccessTokenManager;
import com.ycyw.users.domain.port.service.token.JwtTokenProcessor;
import com.ycyw.users.domain.port.service.token.TokenManager;

import org.eclipse.jdt.annotation.Nullable;

public class JwtAccessTokenManagerImpl implements JwtAccessTokenManager {
  // FIXME: We should use a proper database or cache for storing tokens in real conditions.
  private static final ConcurrentHashMap<UUID, JwtAccessToken> inMemory = new ConcurrentHashMap<>();

  private final JwtTokenProcessor jwtTokenProcessor;

  public JwtAccessTokenManagerImpl(JwtTokenProcessor jwtTokenProcessor) {
    this.jwtTokenProcessor = jwtTokenProcessor;
  }

  /**
   * Mémorise un jeton d'accès si il n'est pas déjà dans la mémoire.
   *
   * @param token Le jeton à mémoriser.
   */
  private static void memorize(JwtAccessToken token) {
    if (!inMemory.containsValue(token)) {
      UUID uuidV7 = UuidV7.randomUuid();
      inMemory.put(uuidV7, token);
    }
  }

  /**
   * Oublie les jetons d'accès en mémoire qui correspondent au filtre.
   *
   * @param filter Le prédicat pour filtrer les entrées à supprimer.
   */
  private static void forgetAny(Predicate<Map.Entry<UUID, JwtAccessToken>> filter) {
    inMemory.entrySet().removeIf(filter);
  }

  @Override
  public JwtAccessToken generate(AccessTokenClaims claims) {
    AccessTokenSubject subject = claims.subject();
    Map<String, String> additional = Collections.singletonMap("role", claims.role());
    JwtTokenProcessor.JwtToken token =
        jwtTokenProcessor.generateJwtToken(subject.value().toString(), additional);
    return new JwtAccessToken(token.value());
  }

  @Override
  public TokenManager.TokenValidity validate(JwtAccessToken token) {
    JwtTokenProcessor.JwtToken jwtToken = toJwtToken(token);
    if (inMemory.containsValue(token)) {
      return new TokenManager.TokenValidity.Invalid(TokenManager.TokenInvalidityReason.REVOKED);
    }
    if (jwtTokenProcessor.hasTokenExpired(jwtToken)) {
      return new TokenManager.TokenValidity.Invalid(TokenManager.TokenInvalidityReason.EXPIRED);
    }
    var result = jwtTokenProcessor.extractApiToken(toJwtToken(token));
    if (result == null) {
      return new TokenManager.TokenValidity.Valid();
    } else {
      return new TokenManager.TokenValidity.Invalid(TokenManager.TokenInvalidityReason.UNKNOWN);
    }
  }

  @Override
  public @Nullable AccessTokenClaims extract(JwtAccessToken token) {
    var claims = jwtTokenProcessor.extractApiToken(toJwtToken(token));
    if (claims == null) {
      return null;
    }
    @Nullable String role = claims.additional().get("role");
    if (role == null || role.isEmpty()) {
      role = "user"; // Default role if not specified
    }
    return new AccessTokenClaims(new AccessTokenSubject(UUID.fromString(claims.subject())), role);
  }

  @Override
  public void invalidate(JwtAccessToken token) {
    memorize(token);
    removeInMemoryExpiredTokens();
  }

  /** Supprime de la mémoire les jetons expirés. */
  private void removeInMemoryExpiredTokens() {
    forgetAny(entry -> jwtTokenProcessor.hasTokenExpired(toJwtToken(entry.getValue())));
  }

  /**
   * Convertit un JwtAccessToken en un JwtToken.
   *
   * @param token Le jeton d'accès à convertir.
   * @return Le jeton JWT correspondant.
   */
  private static JwtTokenProcessor.JwtToken toJwtToken(JwtAccessToken token) {
    return new JwtTokenProcessor.JwtToken(token.value());
  }
}
