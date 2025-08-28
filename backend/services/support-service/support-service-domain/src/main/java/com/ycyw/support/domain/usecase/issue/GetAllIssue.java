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
import com.ycyw.shared.ddd.objectvalues.Address;
import com.ycyw.shared.ddd.objectvalues.BirthDate;
import com.ycyw.shared.ddd.objectvalues.Email;
import com.ycyw.shared.ddd.objectvalues.PhoneNumber;
import com.ycyw.shared.ddd.objectvalues.acriss.AcrissCode;
import com.ycyw.support.domain.model.entity.conversation.Conversation;
import com.ycyw.support.domain.model.entity.externals.agency.Agency;
import com.ycyw.support.domain.model.entity.externals.client.Client;
import com.ycyw.support.domain.model.entity.externals.client.ClientId;
import com.ycyw.support.domain.model.entity.externals.reservation.Reservation;
import com.ycyw.support.domain.model.entity.externals.reservation.ReservationId;
import com.ycyw.support.domain.model.entity.issue.Issue;
import com.ycyw.support.domain.model.entity.issue.IssueId;
import com.ycyw.support.domain.model.valueobject.IssueStatus;
import com.ycyw.support.domain.port.directory.ClientDirectory;
import com.ycyw.support.domain.port.directory.ReservationDirectory;
import com.ycyw.support.domain.port.repository.ConversationRepository;
import com.ycyw.support.domain.port.repository.IssueRepository;
import com.ycyw.support.domain.usecase.issue.GetAllIssue.Output.ClientDto;
import com.ycyw.support.domain.usecase.issue.GetAllIssue.Output.ReservationDto;
import com.ycyw.support.domain.usecase.issue.GetAllIssue.Output.ReservationDto.ArrivingToDto;
import com.ycyw.support.domain.usecase.issue.GetAllIssue.Output.ReservationDto.StartingFromDto;
import com.ycyw.support.domain.usecase.issue.GetAllIssue.Output.ReservationDto.VehiculeDto;

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
        @Nullable String answer,
        IssueStatus status,
        ClientDto client,
        @Nullable ReservationDto reservation,
        @Nullable UUID conversation,
        ZonedDateTime updatedAt) {}

    record ClientDto(
        ClientId id,
        String firstName,
        String lastName,
        Email email,
        PhoneNumber phone,
        BirthDate birthdate,
        Address address) {}

    record ReservationDto(
        UUID id,
        String status,
        StartingFromDto from,
        ArrivingToDto to,
        VehiculeDto vehicule,
        String payment) {
      public record StartingFromDto(Agency agency, ZonedDateTime at) {}

      public record ArrivingToDto(Agency agency, ZonedDateTime at) {}

      public record VehiculeDto(AcrissCode category) {}
    }
  }

  final class Handler implements UseCaseHandler<Input, Output>, GetAllIssue {
    private final IssueRepository issueRepository;
    private final ClientDirectory clientDirectory;
    private final ReservationDirectory reservationDirectory;
    private final ConversationRepository conversationRepository;

    public Handler(
        IssueRepository issueRepository,
        ClientDirectory clientDirectory,
        ReservationDirectory reservationDirectory,
        ConversationRepository conversationRepository) {
      this.issueRepository = issueRepository;
      this.clientDirectory = clientDirectory;
      this.reservationDirectory = reservationDirectory;
      this.conversationRepository = conversationRepository;
    }

    @Override
    public Output handle(Input unused) {

      final var issues = issueRepository.findAll();
      final var clients = mapToClients(issues);
      final var reservations = mapToReservations(issues);

      final Set<IssueAgregate> agregates = new HashSet<>();

      for (Issue issue : issues) {
        final var clientId = issue.getClient();
        final var reservationId = issue.getReservation();

        @Nullable final Client client = clients.get(clientId);
        @Nullable final Reservation reservation = reservations.get(reservationId);

        if (client == null) {
          throw new IllegalDomainStateException("No client found for id " + clientId);
        }

        final var issueId = new IssueId(issue.getId());
        @Nullable Conversation conversation = conversationRepository.findByIssueId(issueId);

        agregates.add(new IssueAgregate(issue, client, reservation, conversation));
      }

      return new Output.All(agregates.stream().map(this::mapToDto).toList());
    }

    private Output.IssueDto mapToDto(IssueAgregate agregate) {
      final Issue issue = agregate.issue;
      final Client client = agregate.client;
      @Nullable final Reservation reservation = agregate.reservation;
      @Nullable final Conversation conversation = agregate.conversation;
      return new Output.IssueDto(
          issue.getId(),
          issue.getSubject(),
          issue.getDescription(),
          issue.getAnswer(),
          issue.getStatus(),
          mapToDto(client),
          mapToDto(reservation),
          conversation != null ? conversation.getId() : null,
          issue.getUpdatedAt());
    }

    private ClientDto mapToDto(Client client) {
      return new ClientDto(
          client.id(),
          client.firstName(),
          client.lastName(),
          client.email(),
          client.phone(),
          client.birthdate(),
          client.address());
    }

    private @Nullable ReservationDto mapToDto(@Nullable Reservation reservation) {
      if (reservation == null) {
        return null;
      }

      final var startingFrom = reservation.from();
      final var arrivingTo = reservation.to();
      final var vehicule = reservation.vehicule();

      return new Output.ReservationDto(
          reservation.id().value(),
          reservation.status(),
          new StartingFromDto(startingFrom.agency(), startingFrom.at()),
          new ArrivingToDto(arrivingTo.agency(), arrivingTo.at()),
          new VehiculeDto(vehicule.category()),
          reservation.payment());
    }

    private Map<ReservationId, Reservation> mapToReservations(List<Issue> entities) {
      Set<ReservationId> idSet = new HashSet<>();
      for (Issue issue : entities) {
        final @Nullable ReservationId id = issue.getReservation();
        if (id != null) {
          idSet.add(id);
        }
      }

      Map<ReservationId, Reservation> output = new HashMap<>();
      for (ReservationId id : idSet) {
        @Nullable Reservation reservation = reservationDirectory.findById(id);
        if (reservation != null) {
          output.put(id, reservation);
        }
      }

      return output;
    }

    private Map<ClientId, Client> mapToClients(List<Issue> entities) {
      Set<ClientId> idSet = new HashSet<>();
      for (Issue issue : entities) {
        idSet.add(issue.getClient());
      }

      Map<ClientId, Client> output = new HashMap<>();
      for (ClientId id : idSet) {
        @Nullable Client client = clientDirectory.find(id);
        if (client != null) {
          output.put(id, client);
        }
      }

      return output;
    }

    private record IssueAgregate(
        Issue issue,
        Client client,
        @Nullable Reservation reservation,
        @Nullable Conversation conversation) {}
  }
}
