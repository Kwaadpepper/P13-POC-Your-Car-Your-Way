package com.ycyw.support.infrastructure.adapter.directory.conversation;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.ycyw.support.domain.model.entity.conversation.ConversationMessage;
import com.ycyw.support.domain.model.valueobject.conversation.MessageSender;
import com.ycyw.support.domain.port.directory.MessageDirectory;
import com.ycyw.support.infrastructure.adapter.service.messages.MessageService;

import org.eclipse.jdt.annotation.Nullable;

@Component
public class MessageQueryDirectory implements MessageDirectory {
  private final MessageService messageRepository;

  public MessageQueryDirectory(MessageService messageRepository) {
    this.messageRepository = messageRepository;
  }

  @Override
  public @Nullable ConversationMessage find(UUID id) {
    final MessageService.@Nullable Message message = messageRepository.find(id);
    if (message == null) {
      return null;
    }
    return toDomain(message);
  }

  @Override
  public List<ConversationMessage> findAll(UUID conversationId) {
    return messageRepository.findAll(conversationId).stream().map(this::toDomain).toList();
  }

  @Override
  public @Nullable ConversationMessage findLatestMessageForConversation(UUID conversationId) {
    final MessageService.@Nullable Message message =
        messageRepository.findLatestMessageForConversation(conversationId);
    if (message == null) {
      return null;
    }
    return toDomain(message);
  }

  private ConversationMessage toDomain(MessageService.Message m) {
    return ConversationMessage.hydrate(
        m.id(), m.conversation(), m.message(), new MessageSender(m.senderType(), m.sender()));
  }
}
