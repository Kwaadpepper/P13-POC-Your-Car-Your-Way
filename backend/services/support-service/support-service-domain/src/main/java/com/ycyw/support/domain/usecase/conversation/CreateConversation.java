package com.ycyw.support.domain.usecase.conversation;

import java.util.UUID;

import com.ycyw.shared.ddd.exceptions.DomainConstraintException;
import com.ycyw.shared.ddd.lib.UseCaseHandler;
import com.ycyw.shared.ddd.lib.UseCaseInput;
import com.ycyw.shared.ddd.lib.UseCaseOutput;
import com.ycyw.support.domain.model.entity.conversation.Conversation;
import com.ycyw.support.domain.model.entity.issue.IssueId;
import com.ycyw.support.domain.port.repository.ConversationRepository;
import com.ycyw.support.domain.port.repository.IssueRepository;

public sealed interface CreateConversation {
  record CreateInput(String subject, UUID issue) implements UseCaseInput, CreateConversation {}

  record Created(UUID id) implements UseCaseOutput, CreateConversation {}

  final class CreateConversationHandler
      implements UseCaseHandler<CreateInput, Created>, CreateConversation {
    private final IssueRepository issueRepository;
    private final ConversationRepository conversationRepository;

    public CreateConversationHandler(
        IssueRepository issueRepository, ConversationRepository conversationRepository) {
      this.issueRepository = issueRepository;
      this.conversationRepository = conversationRepository;
    }

    @Override
    public Created handle(CreateInput usecaseInput) {
      final var subject = usecaseInput.subject();
      final var issue = usecaseInput.issue();
      final var issueId = new IssueId(issue);

      if (issueRepository.find(issue) == null) {
        throw new DomainConstraintException("the provided issue does not exists");
      }

      final var conversation = new Conversation(subject, issueId);

      conversationRepository.save(conversation);

      return new Created(conversation.getId());
    }
  }
}
