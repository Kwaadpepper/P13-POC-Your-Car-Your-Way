package com.ycyw.users.domain.usecase.client;

import java.time.ZonedDateTime;
import java.util.UUID;

import com.ycyw.shared.ddd.lib.UseCaseHandler;
import com.ycyw.shared.ddd.lib.UseCaseInput;
import com.ycyw.shared.ddd.lib.UseCaseOutput;
import com.ycyw.shared.ddd.objectvalues.BirthDate;
import com.ycyw.shared.ddd.objectvalues.Email;
import com.ycyw.users.domain.model.entity.client.Client;
import com.ycyw.users.domain.model.valueobject.Address;
import com.ycyw.users.domain.port.repository.ClientRepository;
import com.ycyw.users.domain.usecase.client.FindClient.Output.FoundClient;

import org.eclipse.jdt.annotation.Nullable;

public sealed interface FindClient {
  sealed interface Input extends UseCaseInput, FindClient {
    record FindById(UUID userId) implements Input {}

    record FindByEmail(Email email) implements Input {}
  }

  sealed interface Output extends UseCaseOutput, FindClient {
    record FoundClient(
        UUID id,
        String lastName,
        String firstName,
        Email email,
        String phone,
        BirthDate birthDate,
        Address address,
        ZonedDateTime updatedAt,
        @Nullable ZonedDateTime deletedAt)
        implements Output {}

    record NotFound() implements Output {}
  }

  final class Handler implements UseCaseHandler<Input, Output>, FindClient {
    private final ClientRepository clientRepository;

    public Handler(ClientRepository clientRepository) {
      this.clientRepository = clientRepository;
    }

    @Override
    public Output handle(Input usecaseInput) {
      return switch (usecaseInput) {
        case Input.FindById getUserById -> run(getUserById);
        case Input.FindByEmail getUserByEmail -> run(getUserByEmail);
      };
    }

    private Output run(Input.FindById usecaseInput) {
      final var id = usecaseInput.userId();

      final var client = clientRepository.find(id);

      if (client == null) {
        return new Output.NotFound();
      }

      return mapToFoundClient(client);
    }

    private Output run(Input.FindByEmail usecaseInput) {
      final var email = usecaseInput.email();

      final var client = clientRepository.findByEmail(email);

      if (client == null) {
        return new Output.NotFound();
      }

      return mapToFoundClient(client);
    }

    private FoundClient mapToFoundClient(Client client) {
      return new FoundClient(
          client.getId(),
          client.getLastName(),
          client.getFirstName(),
          client.getEmail(),
          client.getPhone(),
          client.getBirthDate(),
          client.getAddress(),
          client.getUpdatedAt(),
          client.getDeletedAt());
    }
  }
}
