package com.ycyw.users.domain.port.service.token;

import java.util.Map;

import org.eclipse.jdt.annotation.Nullable;

public interface JwtTokenProcessor {
  @Nullable TokenClaims extractApiToken(JwtToken jwtToken);

  JwtToken generateJwtToken(String subject, Map<String, String> additional);

  boolean hasTokenExpired(JwtToken jwtToken);

  public record TokenClaims(
      String subject, String jti, String iss, long iat, long exp, Map<String, String> additional) {}

  public record JwtToken(String value) {}
}
