package com.ycyw.support.domain.usecase.issue;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.ycyw.shared.ddd.exceptions.IllegalDomainStateException;
import com.ycyw.shared.ddd.lib.UseCaseHandler;
import com.ycyw.shared.ddd.lib.UseCaseInput;
import com.ycyw.shared.ddd.lib.UseCaseOutput;
import com.ycyw.support.domain.model.entity.externals.client.Client;
import com.ycyw.support.domain.model.entity.externals.client.ClientId;
import com.ycyw.support.domain.model.entity.externals.reservation.ReservationId;
import com.ycyw.support.domain.model.entity.issue.Issue;
import com.ycyw.support.domain.model.valueobject.IssueStatus;
import com.ycyw.support.domain.port.directory.ClientDirectory;
import com.ycyw.support.domain.port.repository.IssueRepository;

import org.eclipse.jdt.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        Client client,
        @Nullable UUID reservation,
        ZonedDateTime updatedAt) {}
  }

  final class Handler implements UseCaseHandler<Input, Output>, GetAllIssue {
    private final Logger logger = LoggerFactory.getLogger(GetAllIssue.Handler.class);
    private final IssueRepository issueRepository;
    private final ClientDirectory clientDirectory;

    public Handler(IssueRepository issueRepository, ClientDirectory clientDirectory) {
      this.issueRepository = issueRepository;
      this.clientDirectory = clientDirectory;
    }

    @Override
    public Output handle(Input unused) {

      final var entities = issueRepository.findAll();
      final var clients = mapToClients(entities);

      final Set<IssueAgregate> agregates = new HashSet<>();

      for (Issue entity : entities) {
        final var clientId = entity.getClient();

        @Nullable final Client client = clients.get(clientId);
        if (client != null) {
          agregates.add(new IssueAgregate(entity, client));
        } else {
          logger.error("No client found for id {}", clientId);
          throw new IllegalDomainStateException("No client found for id " + clientId);
        }
      }

      return new Output.All(agregates.stream().map(this::mapToDto).toList());
    }

    private Output.IssueDto mapToDto(IssueAgregate agregate) {
      final Issue issue = agregate.issue;
      final Client client = agregate.client;
      @Nullable final ReservationId reservation = issue.getReservation();
      return new Output.IssueDto(
          issue.getId(),
          issue.getSubject(),
          issue.getDescription(),
          issue.getStatus(),
          client,
          reservation != null ? reservation.value() : null,
          issue.getUpdatedAt());
    }

    private Map<ClientId, Client> mapToClients(List<Issue> entities) {
      Set<UUID> clientIds = new HashSet<>();
      for (Issue issue : entities) {
        clientIds.add(issue.getClient().value());
      }
      Map<ClientId, Client> clients = new HashMap<>();
      for (UUID id : clientIds) {
        @Nullable Client client = clientDirectory.findById(id);
        if (client != null) {
          clients.put(new ClientId(id), client);
        }
      }
      return clients;
    }

    private record IssueAgregate(Issue issue, Client client) {}
  }
}
