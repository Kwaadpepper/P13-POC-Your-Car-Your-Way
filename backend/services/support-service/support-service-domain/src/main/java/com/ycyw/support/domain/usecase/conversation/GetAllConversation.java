package com.ycyw.support.domain.usecase.conversation;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import com.ycyw.shared.ddd.lib.UseCaseHandler;
import com.ycyw.shared.ddd.lib.UseCaseInput;
import com.ycyw.shared.ddd.lib.UseCaseOutput;
import com.ycyw.support.domain.model.entity.conversation.Conversation;
import com.ycyw.support.domain.model.entity.conversation.ConversationMessage;
import com.ycyw.support.domain.port.directory.MessageDirectory;
import com.ycyw.support.domain.port.repository.ConversationRepository;

import org.eclipse.jdt.annotation.Nullable;

public sealed interface GetAllConversation {
  sealed interface Input extends UseCaseInput, GetAllConversation {
    record GetAll() implements Input {}
  }

  sealed interface Output extends UseCaseOutput, GetAllConversation {
    record All(List<ConversationDto> results) implements Output {}

    record ConversationDto(UUID id, String subject, UUID issue, @Nullable LastMessage lastMessage) {
      public record LastMessage(String content, ZonedDateTime sentAt) {}
    }
  }

  final class Handler implements UseCaseHandler<Input, Output>, GetAllConversation {
    private final ConversationRepository conversationRepository;
    private final MessageDirectory messageDirectory;

    public Handler(
        ConversationRepository conversationRepository, MessageDirectory messageDirectory) {
      this.conversationRepository = conversationRepository;
      this.messageDirectory = messageDirectory;
    }

    @Override
    public Output handle(Input unused) {
      final var entities = conversationRepository.findAll();

      return new Output.All(entities.stream().map(this::mapToDto).toList());
    }

    private Output.ConversationDto mapToDto(Conversation entity) {
      final var conversationId = entity.getId();
      final var issue = entity.getIssue();

      @Nullable final ConversationMessage lastMessage =
          messageDirectory.findLatestMessageForConversation(conversationId);

      return new Output.ConversationDto(
          entity.getId(), entity.getSubject(), issue.value(), mapToLastMessageDto(lastMessage));
    }

    private Output.ConversationDto.@Nullable LastMessage mapToLastMessageDto(
        @Nullable ConversationMessage message) {
      if (message == null) {
        return null;
      }
      return new Output.ConversationDto.LastMessage(message.getContent(), message.getSentAt());
    }
  }
}
