package com.ycyw.users.infrastructure.adapter.service.token;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import com.ycyw.users.domain.port.service.token.JwtTokenProcessor;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.eclipse.jdt.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JwtTokenProcessorImpl implements JwtTokenProcessor {
  private static final Logger logger = LoggerFactory.getLogger(JwtTokenProcessorImpl.class);
  private final long jwtExpirationInSeconds;
  private final String jwtIssuer;
  private final SecretKey jwtSigningKey;

  public JwtTokenProcessorImpl(
      long jwtExpirationInSeconds, String jwtSigningKey, String jwtIssuer) {
    this.jwtExpirationInSeconds = jwtExpirationInSeconds;
    this.jwtIssuer = jwtIssuer;
    this.jwtSigningKey = keyFromString(jwtSigningKey);
  }

  /** Extracts the apiToken from a JwtToken that was set in the subject claim. */
  @Override
  public JwtTokenProcessor.@Nullable TokenClaims extractApiToken(
      JwtTokenProcessor.JwtToken jwtToken) {
    try {
      JwtTokenProcessor.TokenClaims claims = extractClaim(jwtToken, this::extractClaimResolver);
      if (claims == null) {
        throw new JwtException("Failed to extract claims from JWT token");
      }
      return claims;
    } catch (Exception e) {
      logger.debug("Error while extracting apiToken from JWT token : {}", e.getMessage());
      return null;
    }
  }

  /** Generates a JwtToken with an apiToken in the subject claim. */
  @Override
  public JwtTokenProcessor.JwtToken generateJwtToken(
      String subject, Map<String, String> additional) {
    String token = generateToken(subject, additional);
    return new JwtTokenProcessor.JwtToken(token);
  }

  /** Checks if a JwtToken is expired. */
  @Override
  public boolean hasTokenExpired(JwtTokenProcessor.JwtToken jwtToken) {
    try {
      var expDate = extractExpiration(jwtToken);
      if (expDate == null) {
        logger.error("Expiration claim is missing in JWT token");
        return true;
      }
      return expDate.toInstant().isBefore(Instant.now());
    } catch (ExpiredJwtException e) {
      return true;
    } catch (Exception e) {
      logger.error("Error while checking if JWT token has expired", e);
      return false;
    }
  }

  private JwtTokenProcessor.TokenClaims extractClaimResolver(Claims claims) {
    @SuppressWarnings("JavaUtilDate")
    var issuedAt = claims.getIssuedAt().getTime();
    @SuppressWarnings("JavaUtilDate")
    var exp = claims.getExpiration().getTime();
    return new JwtTokenProcessor.TokenClaims(
        claims.getSubject(),
        claims.getId(),
        claims.getIssuer(),
        issuedAt,
        exp,
        claims.entrySet().stream()
            .filter(
                entry ->
                    !entry.getKey().equals("sub")
                        && !entry.getKey().equals("jti")
                        && !entry.getKey().equals("iss")
                        && !entry.getKey().equals("iat")
                        && !entry.getKey().equals("exp"))
            .collect(
                Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue().toString())));
  }

  /** Attempts to extract the pay load from JWT token. */
  private Claims extractAllClaims(JwtTokenProcessor.JwtToken jwtToken) {
    return Jwts.parser()
        .verifyWith(jwtSigningKey)
        .requireIssuer(jwtIssuer)
        .build()
        .parseSignedClaims(jwtToken.value())
        .getPayload();
  }

  /** Get a claim value form the token using a resolver. */
  private <T> @Nullable T extractClaim(
      JwtTokenProcessor.JwtToken jwtToken, Function<Claims, T> claimsResolver) {
    Claims claims = extractAllClaims(jwtToken);
    return claimsResolver.apply(claims);
  }

  /** Get expiration date from the token. */
  private @Nullable Date extractExpiration(JwtTokenProcessor.JwtToken jwtToken) {
    return extractClaim(jwtToken, Claims::getExpiration);
  }

  /** Generates and signs the actual JWT token. */
  @SuppressWarnings("null")
  private String generateToken(String subject, Map<String, String> additional) {
    String uniqueJti = UUID.randomUUID().toString();
    ZonedDateTime currentDate = ZonedDateTime.now(ZoneId.systemDefault());
    Date expirationDate = Date.from(currentDate.plusSeconds(jwtExpirationInSeconds).toInstant());

    return Jwts.builder()
        .header()
        .add("typ", "JWT")
        .and()
        .issuer(jwtIssuer)
        .subject(subject)
        .issuedAt(Date.from(currentDate.toInstant()))
        .expiration(expirationDate)
        .id(uniqueJti)
        .claims(additional)
        .signWith(this.jwtSigningKey)
        .compact();
  }

  private SecretKey keyFromString(String base64String) {
    return Keys.hmacShaKeyFor(base64String.getBytes(StandardCharsets.UTF_8));
  }
}
