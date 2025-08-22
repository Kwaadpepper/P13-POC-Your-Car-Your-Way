package com.ycyw.support.domain.usecase.issue;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import com.ycyw.shared.ddd.lib.UseCaseHandler;
import com.ycyw.shared.ddd.lib.UseCaseInput;
import com.ycyw.shared.ddd.lib.UseCaseOutput;
import com.ycyw.support.domain.model.entity.externals.reservation.ReservationId;
import com.ycyw.support.domain.model.entity.issue.Issue;
import com.ycyw.support.domain.model.valueobject.IssueStatus;
import com.ycyw.support.domain.port.repository.IssueRepository;

import org.eclipse.jdt.annotation.Nullable;

public sealed interface GetAllIssue {
  sealed interface Input extends UseCaseInput, GetAllIssue {
    record GetAll() implements Input {}
  }

  sealed interface Output extends UseCaseOutput, GetAllIssue {
    record All(List<IssueDto> results) implements Output {}

    record IssueDto(
        UUID id,
        String subject,
        String description,
        IssueStatus status,
        UUID client,
        @Nullable UUID reservation,
        ZonedDateTime updatedAt) {}
  }

  final class GetAllIssueHandler implements UseCaseHandler<Input, Output>, GetAllIssue {
    private final IssueRepository issueRepository;

    public GetAllIssueHandler(IssueRepository issueRepository) {
      this.issueRepository = issueRepository;
    }

    @Override
    public Output handle(Input unused) {

      final var entities = issueRepository.findAll();

      return new Output.All(entities.stream().map(this::mapToDto).toList());
    }

    private Output.IssueDto mapToDto(Issue entity) {
      @Nullable final ReservationId reservation = entity.getReservation();
      return new Output.IssueDto(
          entity.getId(),
          entity.getSubject(),
          entity.getDescription(),
          entity.getStatus(),
          entity.getClient().value(),
          reservation != null ? reservation.value() : null,
          entity.getUpdatedAt());
    }
  }
}
