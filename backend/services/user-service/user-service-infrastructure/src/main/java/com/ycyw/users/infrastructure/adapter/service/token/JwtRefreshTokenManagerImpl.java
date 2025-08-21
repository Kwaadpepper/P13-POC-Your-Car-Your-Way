package com.ycyw.users.infrastructure.adapter.service.token;

import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

import com.ycyw.users.domain.model.valueobject.jwt.JwtRefreshToken;
import com.ycyw.users.domain.model.valueobject.jwt.RefreshTokenClaims;
import com.ycyw.users.domain.model.valueobject.jwt.RefreshTokenSubject;
import com.ycyw.users.domain.port.service.token.JwtRefreshTokenManager;
import com.ycyw.users.domain.port.service.token.JwtTokenProcessor;
import com.ycyw.users.domain.port.service.token.TokenManager;

import org.eclipse.jdt.annotation.Nullable;

public class JwtRefreshTokenManagerImpl implements JwtRefreshTokenManager {
  // FIXME: We should use a proper database or cache for storing tokens in real conditions.
  private static final ConcurrentHashMap<RefreshTokenSubject, RefreshTokenPair> inMemory =
      new ConcurrentHashMap<>();

  private final JwtTokenProcessor jwtTokenProcessor;

  public JwtRefreshTokenManagerImpl(JwtTokenProcessor jwtTokenProcessor) {
    this.jwtTokenProcessor = jwtTokenProcessor;
  }

  /**
   * Vérifie si un jeton est déjà dans la mémoire.
   *
   * @param token Le jeton à vérifier.
   * @return true si le jeton existe, false sinon.
   */
  private static boolean has(JwtRefreshToken token) {
    return inMemory.values().stream().anyMatch(pair -> pair.token().equals(token));
  }

  /**
   * Recherche un jeton de rafraîchissement par son sujet.
   *
   * @param subject Le sujet du jeton.
   * @return Le jeton de rafraîchissement correspondant, ou null si non trouvé.
   */
  @Override
  public @Nullable JwtRefreshToken find(RefreshTokenSubject subject) {
    @Nullable final RefreshTokenPair foundSubject = inMemory.get(subject);
    return (foundSubject != null) ? foundSubject.token() : null;
  }

  /**
   * Mémorise une paire de jetons de rafraîchissement.
   *
   * @param subject Le sujet du jeton.
   * @param token Le jeton à mémoriser.
   */
  private static void memorize(RefreshTokenSubject subject, JwtRefreshToken token) {
    inMemory.put(subject, new RefreshTokenPair(subject, token));
  }

  /**
   * Oublie un jeton spécifique.
   *
   * @param token Le jeton à oublier.
   */
  private static void forget(JwtRefreshToken token) {
    inMemory.values().removeIf(pair -> pair.token().equals(token));
  }

  /**
   * Oublie les jetons d'accès en mémoire qui correspondent au filtre.
   *
   * @param filter Le prédicat pour filtrer les entrées à supprimer.
   */
  private static void forgetAny(Predicate<Entry<RefreshTokenSubject, RefreshTokenPair>> filter) {
    inMemory.entrySet().removeIf(filter);
  }

  @Override
  public JwtRefreshToken generate(RefreshTokenClaims claims) {
    RefreshTokenSubject subject = claims.subject();
    Map<String, String> additional = Collections.emptyMap();
    JwtTokenProcessor.JwtToken token =
        jwtTokenProcessor.generateJwtToken(subject.value().toString(), additional);
    JwtRefreshToken jwtRefreshToken = new JwtRefreshToken(token.value());
    memorize(subject, jwtRefreshToken);
    return jwtRefreshToken;
  }

  @Override
  public TokenManager.TokenValidity validate(JwtRefreshToken token) {
    JwtTokenProcessor.JwtToken jwtToken = toJwtToken(token);
    if (isRevoked(token)) {
      return new TokenManager.TokenValidity.Invalid(TokenManager.TokenInvalidityReason.REVOKED);
    }
    if (jwtTokenProcessor.hasTokenExpired(jwtToken)) {
      return new TokenManager.TokenValidity.Invalid(TokenManager.TokenInvalidityReason.EXPIRED);
    }
    if (jwtTokenProcessor.extractApiToken(toJwtToken(token)) != null) {
      return new TokenManager.TokenValidity.Valid();
    } else {
      return new TokenManager.TokenValidity.Invalid(TokenManager.TokenInvalidityReason.UNKNOWN);
    }
  }

  @Override
  public @Nullable RefreshTokenClaims extract(JwtRefreshToken token) {
    final var claims = jwtTokenProcessor.extractApiToken(toJwtToken(token));
    if (claims == null) {
      return null;
    }
    return new RefreshTokenClaims(new RefreshTokenSubject(UUID.fromString(claims.subject())));
  }

  @Override
  public void invalidate(JwtRefreshToken token) {
    forget(token);
    forgetExpiredTokens();
  }

  /**
   * Vérifie si un jeton est révoqué.
   *
   * @param token Le jeton à vérifier.
   * @return true si le jeton a été révoqué, false sinon.
   */
  private boolean isRevoked(JwtRefreshToken token) {
    return !has(token);
  }

  /** Supprime les jetons expirés de la mémoire. */
  private void forgetExpiredTokens() {
    forgetAny(entry -> jwtTokenProcessor.hasTokenExpired(toJwtToken(entry.getValue().token())));
  }

  /**
   * Convertit un JwtRefreshToken en un JwtToken.
   *
   * @param token Le jeton de rafraîchissement à convertir.
   * @return Le jeton JWT correspondant.
   */
  private static JwtTokenProcessor.JwtToken toJwtToken(JwtRefreshToken token) {
    return new JwtTokenProcessor.JwtToken(token.value());
  }

  public static record RefreshTokenPair(RefreshTokenSubject subject, JwtRefreshToken token) {}
}
