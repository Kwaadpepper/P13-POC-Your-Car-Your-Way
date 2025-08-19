package com.ycyw.users.application.exception.exceptions;

public class ServerErrorException extends RuntimeException {
  public ServerErrorException(final String message) {
    super(message);
  }

  public ServerErrorException(final String message, final Throwable previous) {
    super(message, previous);
  }
}
