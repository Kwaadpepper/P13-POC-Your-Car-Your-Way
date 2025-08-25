package com.ycyw.support.application.lib;

import java.util.Objects;

import com.ycyw.shared.ddd.lib.DomainEvent;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public final class EventSerializationUtils {
  private EventSerializationUtils() {}

  private static final ObjectMapper objectMapper =
      new ObjectMapper()
          .findAndRegisterModules()
          .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

  public static String serialize(DomainEvent<?> event) {
    try {
      return objectMapper.writeValueAsString(event);
    } catch (Exception e) {
      throw new SerializationException("Erreur de sérialisation de l'événement", e);
    }
  }

  public static <T extends DomainEvent<?>> T deserialize(String json, Class<T> eventClass) {
    try {
      return Objects.requireNonNull(objectMapper.readValue(json, eventClass));
    } catch (Exception e) {
      throw new DeserializationException("Erreur de désérialisation de l'événement", e);
    }
  }

  public static class SerializationException extends RuntimeException {
    public SerializationException(String message, Throwable cause) {
      super(message, cause);
    }
  }

  public static class DeserializationException extends RuntimeException {
    public DeserializationException(String message, Throwable cause) {
      super(message, cause);
    }
  }
}
