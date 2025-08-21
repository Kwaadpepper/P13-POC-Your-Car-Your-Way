package com.ycyw.users.domain.model.valueobject.jwt;

import java.util.regex.Pattern;

import com.ycyw.shared.utils.Domain;

public record JwtRefreshToken(String value) {

  public JwtRefreshToken {
    Domain.checkDomain(() -> !value.isBlank(), "JWT refresh token cannot be blank");
    Domain.checkDomain(() -> isValidJwt(value), "JWT refresh token must be a valid JWT format");
  }

  private boolean isValidJwt(String token) {
    return Pattern.matches("^[A-Za-z0-9-_]+\\.[A-Za-z0-9-_]+\\.[A-Za-z0-9-_]+$", token);
  }
}
