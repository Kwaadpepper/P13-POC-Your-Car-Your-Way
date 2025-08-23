package com.ycyw.support.application.exception.exceptions;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class ValidationException extends RuntimeException {
  private final Set<ValidationError> errors;

  public ValidationException(Set<ValidationError> errors) {
    this.errors = errors;
  }

  public static ValidationException of(Set<ValidationError> invalidFields) {
    return new ValidationException(invalidFields);
  }

  public static ValidationException of(ValidationError... errors) {
    return of(Arrays.stream(errors).collect(Collectors.toSet()));
  }

  public Set<ValidationError> getErrors() {
    return errors;
  }

  public record ValidationError(String field, String message) implements Serializable {
    public static ValidationError of(String field, String message) {
      return new ValidationError(field, message);
    }
  }
}
