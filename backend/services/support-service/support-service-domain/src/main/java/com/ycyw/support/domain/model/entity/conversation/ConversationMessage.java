package com.ycyw.support.domain.model.entity.conversation;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.UUID;

import com.ycyw.shared.ddd.lib.Entity;
import com.ycyw.shared.utils.Domain;
import com.ycyw.shared.utils.UuidV7;
import com.ycyw.support.domain.model.valueobject.conversation.MessageSender;

public class ConversationMessage implements Entity {
  private final UUID id;
  private final String content;
  private final MessageSender sender;

  private static void validateInvariants(String content) {
    Domain.checkDomain(() -> !content.isBlank(), "Message content cannot be blank");
  }

  public ConversationMessage(String content, MessageSender sender) {
    this(UUID.randomUUID(), content, sender);
  }

  protected ConversationMessage(UUID id, String content, MessageSender sender) {
    validateInvariants(content);
    this.id = id;
    this.content = content;
    this.sender = sender;
  }

  public static ConversationMessage hydrate(UUID id, String content, MessageSender sender) {
    return new ConversationMessage(id, content, sender);
  }

  public UUID getId() {
    return id;
  }

  public String getContent() {
    return content;
  }

  public MessageSender getSender() {
    return sender;
  }

  public ZonedDateTime getSentAt() {
    return ZonedDateTime.ofInstant(UuidV7.extractInstant(id), ZoneOffset.UTC);
  }
}
