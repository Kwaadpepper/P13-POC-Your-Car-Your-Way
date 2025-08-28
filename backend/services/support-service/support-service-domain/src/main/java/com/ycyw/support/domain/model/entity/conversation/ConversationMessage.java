package com.ycyw.support.domain.model.entity.conversation;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

import com.ycyw.shared.ddd.lib.Entity;
import com.ycyw.shared.utils.Domain;
import com.ycyw.shared.utils.UuidV7;
import com.ycyw.support.domain.model.valueobject.conversation.MessageSender;

import org.eclipse.jdt.annotation.Nullable;

public class ConversationMessage implements Entity {
  private final UUID id;
  private final UUID conversation;
  private final String content;
  private final MessageSender sender;

  private static void validateInvariants(String content) {
    Domain.checkDomain(() -> !content.isBlank(), "Message content cannot be blank");
  }

  public ConversationMessage(UUID conversation, String content, MessageSender sender) {
    this(UuidV7.randomUuid(), conversation, content, sender);
  }

  protected ConversationMessage(UUID id, UUID conversation, String content, MessageSender sender) {
    validateInvariants(content);
    this.id = id;
    this.conversation = conversation;
    this.content = content;
    this.sender = sender;
  }

  public static ConversationMessage hydrate(
      UUID id, UUID conversation, String content, MessageSender sender) {
    return new ConversationMessage(id, conversation, content, sender);
  }

  public UUID getId() {
    return id;
  }

  public UUID getConversation() {
    return conversation;
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

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof ConversationMessage that)) {
      return false;
    }
    return getId().equals(that.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
