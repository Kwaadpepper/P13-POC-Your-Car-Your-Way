package com.ycyw.support.domain.usecase.conversation;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import com.ycyw.shared.ddd.lib.UseCaseHandler;
import com.ycyw.shared.ddd.lib.UseCaseInput;
import com.ycyw.shared.ddd.lib.UseCaseOutput;
import com.ycyw.support.domain.model.entity.conversation.Conversation;
import com.ycyw.support.domain.model.entity.conversation.ConversationMessage;
import com.ycyw.support.domain.model.valueobject.conversation.MessageSender;
import com.ycyw.support.domain.port.directory.MessageDirectory;
import com.ycyw.support.domain.port.repository.ConversationRepository;

import org.eclipse.jdt.annotation.Nullable;

public sealed interface GetConversationMessages {
  record ForConversation(UUID id) implements UseCaseInput, GetConversationMessages {}

  sealed interface Output extends UseCaseOutput, GetConversationMessages {
    record Messages(List<MessageDto> value) implements Output {
      public record MessageDto(
          UUID id, String content, MessageSender sender, ZonedDateTime sentAt) {}
    }

    record NoSuchConversation() implements Output {}
  }

  final class Handler implements UseCaseHandler<ForConversation, Output>, GetConversationMessages {
    private final ConversationRepository conversationRepository;
    private final MessageDirectory messageDirectory;

    public Handler(
        ConversationRepository conversationRepository, MessageDirectory messageDirectory) {
      this.conversationRepository = conversationRepository;
      this.messageDirectory = messageDirectory;
    }

    @Override
    public Output handle(ForConversation input) {
      final var conversationId = input.id();
      @Nullable Conversation conversation = conversationRepository.find(conversationId);

      if (conversation == null) {
        return new Output.NoSuchConversation();
      }

      final var messages = messageDirectory.findAll(conversationId);

      return new Output.Messages(messages.stream().map(this::mapToDto).toList());
    }

    private Output.Messages.MessageDto mapToDto(ConversationMessage entity) {
      return new Output.Messages.MessageDto(
          entity.getId(), entity.getContent(), entity.getSender(), entity.getSentAt());
    }
  }
}
