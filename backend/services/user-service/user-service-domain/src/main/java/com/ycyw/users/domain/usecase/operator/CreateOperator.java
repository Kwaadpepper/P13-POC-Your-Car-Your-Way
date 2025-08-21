package com.ycyw.users.domain.usecase.operator;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Set;

import com.ycyw.shared.ddd.exceptions.DomainConstraintException;
import com.ycyw.shared.ddd.lib.UseCaseHandler;
import com.ycyw.shared.ddd.lib.UseCaseInput;
import com.ycyw.shared.ddd.lib.UseCaseOutput;
import com.ycyw.users.domain.model.entity.credential.Credential;
import com.ycyw.users.domain.model.entity.credential.CredentialId;
import com.ycyw.users.domain.model.entity.operator.Operator;
import com.ycyw.users.domain.model.valueobject.Email;
import com.ycyw.users.domain.model.valueobject.RawIdentifier;
import com.ycyw.users.domain.model.valueobject.RawPassword;
import com.ycyw.users.domain.model.valueobject.Role;
import com.ycyw.users.domain.port.repository.CredentialRepository;
import com.ycyw.users.domain.port.repository.OperatorRepository;
import com.ycyw.users.domain.port.service.PasswordHasher;
import com.ycyw.users.domain.service.IdentifierHasher;

public sealed interface CreateOperator {
  record CreateOperatorInput(
      String name, Email email, Set<Role> roles, RawIdentifier identifier, RawPassword password)
      implements UseCaseInput, CreateOperator {}

  record CreatedOperator(Email email) implements UseCaseOutput, CreateOperator {}

  final class CreateOperatorHandler
      implements UseCaseHandler<CreateOperatorInput, CreatedOperator>, CreateOperator {
    private final CredentialRepository credentialRepository;
    private final OperatorRepository operatorRepository;
    private final IdentifierHasher identifierHasher;
    private final PasswordHasher passwordHasher;

    public CreateOperatorHandler(
        CredentialRepository credentialRepository,
        OperatorRepository operatorRepository,
        IdentifierHasher identifierHasher,
        PasswordHasher passwordHasher) {
      this.credentialRepository = credentialRepository;
      this.operatorRepository = operatorRepository;
      this.identifierHasher = identifierHasher;
      this.passwordHasher = passwordHasher;
    }

    @Override
    public CreatedOperator handle(CreateOperatorInput usecaseInput) {
      final var name = usecaseInput.name();
      final var email = usecaseInput.email();
      final var roles = usecaseInput.roles();
      final var rawIdentifier = usecaseInput.identifier();
      final var rawPassword = usecaseInput.password();
      final var hashedIdentifier = identifierHasher.hash(rawIdentifier);
      final var hashedPassword = passwordHasher.hash(rawPassword);

      if (credentialRepository.findByIdentifier(hashedIdentifier) != null) {
        throw new DomainConstraintException("User with this identifier already exists.");
      }

      if (operatorRepository.findByEmail(email) != null) {
        throw new DomainConstraintException("User this email already exists.");
      }

      final var credential =
          new Credential(hashedPassword, hashedIdentifier, null, null, null, null);

      credentialRepository.save(credential);

      final var credentialId = new CredentialId(credential.getId());
      final var operator =
          new Operator(
              name, email, roles, credentialId, ZonedDateTime.now(ZoneId.systemDefault()), null);
      operatorRepository.save(operator);
      return mapToCreatedUser(operator);
    }

    private CreatedOperator mapToCreatedUser(Operator operator) {
      return new CreatedOperator(operator.getEmail());
    }
  }
}
