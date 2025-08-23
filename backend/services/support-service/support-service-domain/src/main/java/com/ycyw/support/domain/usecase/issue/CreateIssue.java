package com.ycyw.support.domain.usecase.issue;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

import com.ycyw.shared.ddd.lib.UseCaseHandler;
import com.ycyw.shared.ddd.lib.UseCaseInput;
import com.ycyw.shared.ddd.lib.UseCaseOutput;
import com.ycyw.support.domain.model.entity.externals.client.ClientId;
import com.ycyw.support.domain.model.entity.externals.reservation.ReservationId;
import com.ycyw.support.domain.model.entity.issue.Issue;
import com.ycyw.support.domain.model.valueobject.IssueStatus;
import com.ycyw.support.domain.port.repository.IssueRepository;

import org.eclipse.jdt.annotation.Nullable;

public sealed interface CreateIssue {
  record CreateInput(
      String subject,
      String description,
      IssueStatus status,
      UUID client,
      @Nullable UUID reservation)
      implements UseCaseInput, CreateIssue {}

  record Created(UUID id) implements UseCaseOutput, CreateIssue {}

  final class Handler implements UseCaseHandler<CreateInput, Created> {
    private final IssueRepository issueRepository;

    public Handler(IssueRepository issueRepository) {
      this.issueRepository = issueRepository;
    }

    @Override
    public Created handle(CreateInput usecaseInput) {
      final var subject = usecaseInput.subject();
      final var description = usecaseInput.description();
      final var status = usecaseInput.status();
      final var client = usecaseInput.client();
      @Nullable final UUID reservation = usecaseInput.reservation();

      final var clientId = new ClientId(client);
      @Nullable final ReservationId reservationId =
          reservation != null ? new ReservationId(reservation) : null;

      // TODO: May check client dans reservation exists using a secondary adpater (port).

      final var issue =
          new Issue(
              subject,
              description,
              status,
              clientId,
              reservationId,
              ZonedDateTime.now(ZoneId.systemDefault()));

      issueRepository.save(issue);

      return new Created(issue.getId());
    }
  }
}
