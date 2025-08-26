package com.ycyw.support.domain.usecase.conversation;

import java.util.UUID;

import com.ycyw.shared.ddd.exceptions.DomainConstraintException;
import com.ycyw.shared.ddd.lib.UseCaseHandler;
import com.ycyw.shared.ddd.lib.UseCaseInput;
import com.ycyw.shared.ddd.lib.UseCaseOutput;
import com.ycyw.shared.ddd.lib.event.DomainEventPublisher;
import com.ycyw.support.domain.event.MessageWasAddedToConversation;
import com.ycyw.support.domain.model.entity.conversation.Conversation;
import com.ycyw.support.domain.model.entity.conversation.ConversationMessage;
import com.ycyw.support.domain.model.valueobject.conversation.MessageSender;
import com.ycyw.support.domain.port.repository.ConversationRepository;

import org.eclipse.jdt.annotation.Nullable;

public sealed interface SendMessage {
  record Message(UUID conversation, String content, MessageSender sender) implements UseCaseInput {}

  record Sent(UUID messageId) implements UseCaseOutput {}

  final class Handler implements UseCaseHandler<Message, Sent>, SendMessage {
    private final ConversationRepository conversationRepository;
    private final DomainEventPublisher domainEventPublisher;

    public Handler(
        DomainEventPublisher domainEventPublisher, ConversationRepository conversationRepository) {
      this.conversationRepository = conversationRepository;
      this.domainEventPublisher = domainEventPublisher;
    }

    @Override
    public Sent handle(Message message) {
      final var conversationId = message.conversation;
      final var content = message.content;
      final var sender = message.sender;

      @Nullable final Conversation conversation = conversationRepository.find(conversationId);
      if (conversation == null) {
        throw new DomainConstraintException("Conversation not found: " + conversationId);
      }

      final var newMessage = new ConversationMessage(content, sender);

      conversation.sendMessage(
          addedMessage -> {
            final var payload =
                new MessageWasAddedToConversation.Message(
                    addedMessage.getContent(), addedMessage.getSender());
            final var event = new MessageWasAddedToConversation(payload);

            domainEventPublisher.publish(event);
          },
          newMessage);

      conversationRepository.save(conversation);

      return new Sent(newMessage.getId());
    }
  }
}
