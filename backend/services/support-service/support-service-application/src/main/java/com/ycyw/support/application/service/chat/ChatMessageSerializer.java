package com.ycyw.support.application.service.chat;

import java.util.Objects;

import org.springframework.stereotype.Component;

import com.ycyw.support.application.service.chat.ChatRoomService.ChatMessage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@Component
public final class ChatMessageSerializer {
  private ChatMessageSerializer() {}

  private final ObjectMapper objectMapper =
      new ObjectMapper()
          .findAndRegisterModules()
          .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

  public String serialize(ChatMessage event) {
    try {
      return objectMapper.writeValueAsString(event);
    } catch (Exception e) {
      throw new SerializationException("Erreur de sérialisation du message", e);
    }
  }

  public ChatMessage deserialize(String json) {
    try {
      return Objects.requireNonNull(objectMapper.readValue(json, ChatMessage.class));
    } catch (Exception e) {
      throw new DeserializationException("Erreur de désérialisation du message", e);
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
