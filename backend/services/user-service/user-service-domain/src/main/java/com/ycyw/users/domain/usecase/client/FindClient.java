package com.ycyw.users.domain.usecase.client;

import java.time.ZonedDateTime;
import java.util.UUID;

import com.ycyw.shared.ddd.lib.UseCaseHandler;
import com.ycyw.shared.ddd.lib.UseCaseInput;
import com.ycyw.shared.ddd.lib.UseCaseOutput;
import com.ycyw.users.domain.model.entity.client.Client;
import com.ycyw.users.domain.model.valueobject.Address;
import com.ycyw.users.domain.model.valueobject.BirthDate;
import com.ycyw.users.domain.model.valueobject.Email;
import com.ycyw.users.domain.port.repository.ClientRepository;

import org.eclipse.jdt.annotation.Nullable;

public sealed interface FindClient {
  sealed interface Input extends UseCaseInput, FindClient {
    record FindClientById(UUID userId) implements Input {}

    record FindClientByEmail(Email email) implements Input {}
  }

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
      implements UseCaseOutput, FindClient {}

  final class FindUserHandler implements UseCaseHandler<Input, @Nullable FoundClient>, FindClient {
    private final ClientRepository clientRepository;

    public FindUserHandler(ClientRepository clientRepository) {
      this.clientRepository = clientRepository;
    }

    public @Nullable FoundClient handle(Input usecaseInput) {
      return switch (usecaseInput) {
        case Input.FindClientById getUserById -> run(getUserById);
        case Input.FindClientByEmail getUserByEmail -> run(getUserByEmail);
      };
    }

    private @Nullable FoundClient run(Input.FindClientById usecaseInput) {
      final var id = usecaseInput.userId();

      final var client = clientRepository.find(id);
      return mapToFoundClient(client);
    }

    private @Nullable FoundClient run(Input.FindClientByEmail usecaseInput) {
      final var email = usecaseInput.email();

      final var client = clientRepository.findByEmail(email);
      return mapToFoundClient(client);
    }

    private @Nullable FoundClient mapToFoundClient(@Nullable Client client) {
      if (client == null) {
        return null;
      }
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
