package com.ycyw.support.application.exception.exceptions;

public class ResourceNotFoundException extends RuntimeException {

  public ResourceNotFoundException(final String message) {
    super(message);
  }

  public ResourceNotFoundException(final String message, final Throwable previous) {
    super(message, previous);
  }
}
