package com.ycyw.shared.ddd.objectvalues;

import java.util.regex.Pattern;

import com.ycyw.shared.utils.Domain;

public record JwtAccessToken(String value) {

  public JwtAccessToken {
    Domain.checkDomain(() -> !value.isBlank(), "JWT token cannot be blank");
    Domain.checkDomain(() -> isValidJwt(value), "JWT token must be a valid JWT format");
  }

  private Boolean isValidJwt(String token) {
    String jwtRegex = "^[A-Za-z0-9-_]+\\.[A-Za-z0-9-_]+\\.[A-Za-z0-9-_]+$";
    return Pattern.matches(jwtRegex, token);
  }
}
