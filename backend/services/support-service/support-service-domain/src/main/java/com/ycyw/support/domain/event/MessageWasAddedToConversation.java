package com.ycyw.support.domain.event;

import java.time.Instant;

import com.ycyw.shared.ddd.lib.event.DomainEvent;
import com.ycyw.support.domain.event.MessageWasAddedToConversation.Message;
import com.ycyw.support.domain.model.valueobject.conversation.MessageSender;

public class MessageWasAddedToConversation implements DomainEvent<Message> {
  private final Message payload;
  private final Instant timestamp = Instant.now();

  public MessageWasAddedToConversation(Message payload) {
    this.payload = payload;
  }

  public record Message(String content, MessageSender sender) {}

  @Override
  public String routingKey() {
    return "chat.message.added";
  }

  @Override
  public Message payload() {
    return payload;
  }

  @Override
  public Instant timestamp() {
    return timestamp;
  }
}
