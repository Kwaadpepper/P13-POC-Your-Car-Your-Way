package com.ycyw.support.domain.model.entity.conversation;

import java.util.UUID;

import com.ycyw.shared.utils.Domain;
import com.ycyw.support.domain.model.valueobject.conversation.MessageSender;

public record ConversationMessage(UUID id, String content, MessageSender sender) {
  public ConversationMessage {
    Domain.checkDomain(() -> !content.isBlank(), "Message content cannot be blank");
  }
}
