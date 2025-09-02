package com.ycyw.support.application.seeder;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.ycyw.shared.ddd.lib.UseCaseExecutor;
import com.ycyw.shared.utils.UuidV7;
import com.ycyw.support.domain.model.valueobject.IssueStatus;
import com.ycyw.support.domain.usecase.conversation.CreateConversation;
import com.ycyw.support.domain.usecase.issue.CreateIssue;

import net.datafaker.Faker;
import org.eclipse.jdt.annotation.Nullable;

@Component
public class IssueSeeder implements Seeder {
  private static final int AMOUNT_TO_SEED = 10;

  private final Faker dataFaker;
  private final UseCaseExecutor useCaseExecutor;
  private final CreateIssue.Handler handler;
  private final CreateConversation.Handler createConversationHandler;

  public IssueSeeder(
      Faker dataFaker,
      UseCaseExecutor useCaseExecutor,
      CreateIssue.Handler handler,
      CreateConversation.Handler createConversationHandler) {
    this.dataFaker = dataFaker;
    this.useCaseExecutor = useCaseExecutor;
    this.handler = handler;
    this.createConversationHandler = createConversationHandler;
  }

  @Override
  public void seed() {
    int i = 0;
    while (i < AMOUNT_TO_SEED) {
      createModel();
      i++;
    }
  }

  private void createModel() {
    final var johnDoeClientId = "019159f8-d42f-7000-8000-000000000002";

    var subject = dataFaker.lorem().sentence();
    var description = dataFaker.lorem().paragraph();
    @Nullable IssueStatus status = dataFaker.options().option(IssueStatus.class);
    var client = UUID.fromString(johnDoeClientId);

    // Using random uuid as the reservation directory adapter uses faker.
    @Nullable UUID reservation = UuidV7.randomUuid();

    var useCase = new CreateIssue.CreateInput(subject, description, status, client, reservation);

    var output = useCaseExecutor.execute(this.handler, useCase);

    createConversation(output.id());
  }

  private void createConversation(UUID issue) {

    var subject = dataFaker.lorem().sentence();

    var useCase = new CreateConversation.CreateInput(subject, issue);
    useCaseExecutor.execute(this.createConversationHandler, useCase);
  }
}
