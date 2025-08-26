package com.ycyw.support.infrastructure.event;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.ycyw.shared.ddd.lib.event.DomainEventListener;
import com.ycyw.support.domain.event.MessageWasAddedToConversation;
import com.ycyw.support.infrastructure.adapter.service.messages.MessageService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class NewMessageDomainEventListener
    implements DomainEventListener<MessageWasAddedToConversation> {
  private static Logger logger =
      LoggerFactory.getLogger(NewMessageDomainEventListener.class.getName());

  private final MessageService messageService;

  public NewMessageDomainEventListener(MessageService messageService) {
    this.messageService = messageService;
  }

  @EventListener
  @Override
  public void onEvent(MessageWasAddedToConversation event) {
    if (logger.isDebugEnabled()) {
      logger.debug("New message added to conversation: {}", event.routingKey());
    }
    messageService.save(mapToMessage(event));
  }

  MessageService.Message mapToMessage(MessageWasAddedToConversation event) {
    final var eventMessage = event.payload();
    final var messageSender = eventMessage.sender();
    return new MessageService.Message(
        eventMessage.id(),
        eventMessage.content(),
        eventMessage.conversation(),
        messageSender.id(),
        messageSender.type());
  }
}
