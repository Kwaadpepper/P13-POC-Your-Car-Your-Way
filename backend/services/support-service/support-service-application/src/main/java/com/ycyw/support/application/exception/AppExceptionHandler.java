package com.ycyw.support.application.exception;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.ycyw.shared.ddd.exceptions.DomainConstraintException;
import com.ycyw.shared.ddd.exceptions.IllegalDomainStateException;
import com.ycyw.support.application.dto.ApiErrorDetails;
import com.ycyw.support.application.dto.ValidationErrorDetails;
import com.ycyw.support.application.exception.exceptions.JwtAuthenticationFailureException;
import com.ycyw.support.application.exception.exceptions.ResourceNotFoundException;
import com.ycyw.support.application.exception.exceptions.ValidationException;

@ControllerAdvice
public class AppExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(JwtAuthenticationFailureException.class)
  public ResponseEntity<ApiErrorDetails> handleException(
      final JwtAuthenticationFailureException ex, final WebRequest request) {
    return new ResponseEntity<>(toErrorDetails(ex, request), HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(DomainConstraintException.class)
  public ResponseEntity<ApiErrorDetails> handleException(
      final DomainConstraintException ex, final WebRequest request) {
    return new ResponseEntity<>(toErrorDetails(ex, request), HttpStatus.UNPROCESSABLE_ENTITY);
  }

  @ExceptionHandler(IllegalDomainStateException.class)
  public ResponseEntity<ApiErrorDetails> handleException(
      final IllegalDomainStateException ex, final WebRequest request) {
    return new ResponseEntity<>(toErrorDetails(ex, request), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ApiErrorDetails> handleException(
      final ResourceNotFoundException ex, final WebRequest request) {
    return new ResponseEntity<>(toErrorDetails(ex, request), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(Throwable.class)
  public ResponseEntity<ApiErrorDetails> handleException(
      final Throwable ex, final WebRequest request) {
    logger.fatal("Error : '%s' on uri '%s'".formatted(ex.getMessage(), getRequestUri(request)), ex);
    return new ResponseEntity<>(
        toErrorDetails("Server error", request), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(ValidationException.class)
  public ResponseEntity<ValidationErrorDetails> handleException(
      final ValidationException ex, final WebRequest request) {

    final Map<String, String> errors = new HashMap<>();
    ex.getErrors()
        .forEach(
            error -> {
              final var fieldName = error.field();
              final var errorMessage = error.message();
              errors.put(fieldName, errorMessage);
            });

    return new ResponseEntity<>(
        toValidationErrorDetails("Some fields could not be validated", errors, request),
        HttpStatus.UNPROCESSABLE_ENTITY);
  }

  @Override
  @Nullable protected ResponseEntity<Object> handleHandlerMethodValidationException(
      final HandlerMethodValidationException ex,
      final HttpHeaders headers,
      final HttpStatusCode status,
      final WebRequest request) {

    final Map<String, String> errors = new HashMap<>();
    ex.getParameterValidationResults()
        .forEach(
            error -> {
              final var parameterName = error.getMethodParameter().getParameterName();
              final var fieldName = toSnakeCase(parameterName != null ? parameterName : "unknown");
              final var errorMessage =
                  error.getResolvableErrors().stream()
                      .map(MessageSourceResolvable::getDefaultMessage)
                      .toList()
                      .toString();
              errors.put(fieldName, errorMessage);
            });

    return new ResponseEntity<>(
        toValidationErrorDetails("Some fields could not be validated", errors, request),
        HttpStatus.UNPROCESSABLE_ENTITY);
  }

  @Override
  @Nullable protected ResponseEntity<Object> handleMethodArgumentNotValid(
      final MethodArgumentNotValidException ex,
      final HttpHeaders headers,
      final HttpStatusCode status,
      final WebRequest request) {

    final Map<String, String> errors = new HashMap<>();
    ex.getBindingResult()
        .getAllErrors()
        .forEach(
            error -> {
              final var fieldName = toSnakeCase(((FieldError) error).getField());
              final var errorMessage = error.getDefaultMessage();
              errors.put(fieldName, errorMessage);
            });

    return new ResponseEntity<>(
        toValidationErrorDetails("Some fields could not be validated", errors, request),
        HttpStatus.UNPROCESSABLE_ENTITY);
  }

  private String getRequestUri(final WebRequest request) {
    return ((ServletWebRequest) request).getRequest().getRequestURI();
  }

  private ApiErrorDetails toErrorDetails(final String message, final WebRequest request) {
    return new ApiErrorDetails(
        LocalDate.now(ZoneId.systemDefault()), message, getRequestUri(request));
  }

  private ApiErrorDetails toErrorDetails(final Throwable e, final WebRequest request) {
    return new ApiErrorDetails(
        LocalDate.now(ZoneId.systemDefault()),
        e.getMessage() != null ? e.getMessage() : "error",
        getRequestUri(request));
  }

  private String toSnakeCase(final String value) {
    return value.replaceAll("(.)(\\p{Upper}+|\\d+)", "$1_$2").toLowerCase(Locale.ROOT);
  }

  private ValidationErrorDetails toValidationErrorDetails(
      final String message, final Map<String, String> errors, final WebRequest request) {
    return new ValidationErrorDetails(
        LocalDate.now(ZoneId.systemDefault()), message, errors, getRequestUri(request));
  }
}
