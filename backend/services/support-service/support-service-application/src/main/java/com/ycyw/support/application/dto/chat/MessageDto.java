package com.ycyw.support.application.dto.chat;

import java.util.UUID;

public record MessageDto(
    UUID id, UUID conversation, UserDto from, String text, java.time.ZonedDateTime sentAt) {

  public static record UserDto(UUID id, String role) {}
}
