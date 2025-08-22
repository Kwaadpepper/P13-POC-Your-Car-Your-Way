package com.ycyw.support.domain.usecase.conversation;

import java.util.List;
import java.util.UUID;

import com.ycyw.shared.ddd.lib.UseCaseHandler;
import com.ycyw.shared.ddd.lib.UseCaseInput;
import com.ycyw.shared.ddd.lib.UseCaseOutput;
import com.ycyw.support.domain.model.entity.conversation.Conversation;
import com.ycyw.support.domain.model.entity.issue.IssueId;
import com.ycyw.support.domain.port.repository.ConversationRepository;

public sealed interface GetAllConversation {
  sealed interface Input extends UseCaseInput, GetAllConversation {
    record GetAll() implements Input {}
  }

  sealed interface Output extends UseCaseOutput, GetAllConversation {
    record All(List<ConversationDto> results) implements Output {}

    record ConversationDto(UUID id, String subject, UUID issue) {}
  }

  final class GetAllConversationHandler
      implements UseCaseHandler<Input, Output>, GetAllConversation {
    private final ConversationRepository conversationRepository;

    public GetAllConversationHandler(ConversationRepository conversationRepository) {
      this.conversationRepository = conversationRepository;
    }

    @Override
    public Output handle(Input unused) {

      final var entities = conversationRepository.findAll();

      return new Output.All(entities.stream().map(this::mapToDto).toList());
    }

    private Output.ConversationDto mapToDto(Conversation entity) {
      final IssueId issue = entity.getIssue();
      return new Output.ConversationDto(entity.getId(), entity.getSubject(), issue.value());
    }
  }
}
