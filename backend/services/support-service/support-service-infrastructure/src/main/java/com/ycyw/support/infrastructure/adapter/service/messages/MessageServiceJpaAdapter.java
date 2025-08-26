package com.ycyw.support.infrastructure.adapter.service.messages;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.ycyw.support.domain.model.valueobject.conversation.SenderType;
import com.ycyw.support.infrastructure.adapter.repository.jpa.messages.MessageJpaRepository;
import com.ycyw.support.infrastructure.entity.MessageEntity;

import org.eclipse.jdt.annotation.Nullable;

@Service
public class MessageServiceJpaAdapter implements MessageService {

  private final MessageJpaRepository repo;

  public MessageServiceJpaAdapter(MessageJpaRepository repo) {
    this.repo = repo;
  }

  @Override
  public List<Message> findAll(UUID conversation) {
    return repo.findByConversationId(conversation).stream().map(this::toDomain).toList();
  }

  @Override
  public @Nullable Message find(UUID message) {
    final var e = repo.findById(message);
    if (e.isEmpty()) {
      return null;
    }
    return e.map(this::toDomain).orElseThrow();
  }

  @Override
  public void save(Message message) {
    repo.save(toEntity(message));
  }

  private MessageEntity toEntity(Message m) {
    final var e = new MessageEntity();
    e.setId(m.id());
    e.setMessage(m.message());
    e.setConversationId(m.conversation());
    e.setSenderId(m.sender());
    e.setSenderType(m.senderType().name());
    return e;
  }

  private Message toDomain(MessageEntity e) {
    return new Message(
        e.getId(),
        e.getMessage(),
        e.getConversationId(),
        e.getSenderId(),
        toSenderType(e.getSenderType()));
  }

  private SenderType toSenderType(String s) {
    return switch (s) {
      case "CLIENT" -> SenderType.CLIENT;
      case "OPERATOR" -> SenderType.OPERATOR;
      default -> throw new IllegalArgumentException("Unknown sender type: " + s);
    };
  }
}
