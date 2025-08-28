package com.ycyw.support.application.service.event.eventsdtos;

import java.io.Serializable;
import java.util.UUID;

public record PresenceEvent(UUID conversationId, UUID userId, String role, Status status)
    implements Serializable {
  public enum Status {
    ONLINE("online"),
    OFFLINE("offline");

    private final String value;

    Status(String value) {
      this.value = value;
    }

    public String value() {
      return value;
    }
  }
}
