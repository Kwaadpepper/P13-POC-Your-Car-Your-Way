package com.ycyw.users.domain.usecase.client;

import java.time.ZonedDateTime;

import com.ycyw.shared.ddd.exceptions.DomainConstraintException;
import com.ycyw.shared.ddd.lib.UseCaseHandler;
import com.ycyw.shared.ddd.lib.UseCaseInput;
import com.ycyw.shared.ddd.lib.UseCaseOutput;
import com.ycyw.users.domain.model.entity.client.Client;
import com.ycyw.users.domain.model.entity.credential.Credential;
import com.ycyw.users.domain.model.entity.credential.CredentialId;
import com.ycyw.users.domain.model.valueobject.Address;
import com.ycyw.users.domain.model.valueobject.BirthDate;
import com.ycyw.users.domain.model.valueobject.Email;
import com.ycyw.users.domain.model.valueobject.HashedIdentifier;
import com.ycyw.users.domain.model.valueobject.RawIdentifier;
import com.ycyw.users.domain.model.valueobject.RawPassword;
import com.ycyw.users.domain.port.repository.ClientRepository;
import com.ycyw.users.domain.port.repository.CredentialRepository;
import com.ycyw.users.domain.port.service.Hasher;
import com.ycyw.users.domain.port.service.PasswordHasher;

public sealed interface CreateClient {
  record CreateClientInput(
      String lastName,
      String firstName,
      Email email,
      String phone,
      BirthDate birthDate,
      Address address,
      RawIdentifier identifier,
      RawPassword password)
      implements UseCaseInput, CreateClient {}

  record CreatedClient(Email email) implements UseCaseOutput, CreateClient {}

  final class CreateClientHandler
      implements UseCaseHandler<CreateClientInput, CreatedClient>, CreateClient {
    private final CredentialRepository credentialRepository;
    private final ClientRepository clientRepository;
    private final Hasher hasher;
    private final PasswordHasher passwordHasher;

    public CreateClientHandler(
        CredentialRepository credentialRepository,
        ClientRepository clientRepository,
        Hasher hasher,
        PasswordHasher passwordHasher) {
      this.credentialRepository = credentialRepository;
      this.clientRepository = clientRepository;
      this.hasher = hasher;
      this.passwordHasher = passwordHasher;
    }

    public CreatedClient handle(CreateClientInput usecaseInput) {
      final var lastName = usecaseInput.lastName();
      final var firstName = usecaseInput.firstName();
      final var email = usecaseInput.email();
      final var phone = usecaseInput.phone();
      final var birthDate = usecaseInput.birthDate();
      final var address = usecaseInput.address();
      final var rawIdentifier = usecaseInput.identifier();
      final var rawPassword = usecaseInput.password();
      final var hashedIdentifier =
          new HashedIdentifier(hasher.hash(rawIdentifier.value(), Hasher.HashAlgorithm.SHA256));
      final var hashedPassword = passwordHasher.hash(rawPassword);

      if (credentialRepository.findByIdentifier(hashedIdentifier) != null) {
        throw new DomainConstraintException("User with this identifier already exists.");
      }

      if (clientRepository.findByEmail(email) != null) {
        throw new DomainConstraintException("User this email already exists.");
      }

      final var credential =
          new Credential(hashedPassword, hashedIdentifier, null, null, null, null);

      credentialRepository.save(credential);

      final var credentialId = new CredentialId(credential.getId());
      final var client =
          new Client(
              lastName,
              firstName,
              email,
              phone,
              birthDate,
              address,
              credentialId,
              ZonedDateTime.now(),
              null);
      clientRepository.save(client);
      return mapToCreatedUser(client);
    }

    private CreatedClient mapToCreatedUser(Client client) {
      return new CreatedClient(client.getEmail());
    }
  }
}
