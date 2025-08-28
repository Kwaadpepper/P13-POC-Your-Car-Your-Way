package com.ycyw.support.application.dto;

import java.time.ZonedDateTime;
import java.util.UUID;

import org.eclipse.jdt.annotation.Nullable;

public record ConversationDto(
    UUID id, String subject, UUID issue, @Nullable LastMessage lastMessage) {
  public record LastMessage(String content, ZonedDateTime sentAt) {}
}
