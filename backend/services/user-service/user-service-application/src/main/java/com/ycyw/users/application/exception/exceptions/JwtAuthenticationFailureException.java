package com.ycyw.users.application.exception.exceptions;

import org.springframework.security.core.AuthenticationException;

public class JwtAuthenticationFailureException extends AuthenticationException {

  public JwtAuthenticationFailureException(final String message) {
    super(message);
  }

  public JwtAuthenticationFailureException(final String message, final Exception previous) {
    super(message, previous);
  }
}
