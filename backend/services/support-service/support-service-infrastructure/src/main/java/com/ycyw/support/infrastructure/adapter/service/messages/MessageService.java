package com.ycyw.support.infrastructure.adapter.service.messages;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import com.ycyw.shared.utils.UuidV7;
import com.ycyw.support.domain.model.valueobject.conversation.SenderType;

import org.eclipse.jdt.annotation.Nullable;

public interface MessageService {
  List<Message> findAll(UUID conversation);

  @Nullable Message find(UUID message);

  @Nullable Message findLatestMessageForConversation(UUID conversationId);

  void save(Message message);

  record Message(UUID id, String message, UUID conversation, UUID sender, SenderType senderType) {
    ZonedDateTime createdAt() {
      return UuidV7.extractInstant(id).atZone(ZoneId.systemDefault());
    }
  }
}
