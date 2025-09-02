package com.ycyw.users.application.exception.exceptions;

import org.springframework.security.core.AuthenticationException;

public class AuthenticationFailureException extends AuthenticationException {
  public AuthenticationFailureException(String msg) {
    super(msg);
  }
}
