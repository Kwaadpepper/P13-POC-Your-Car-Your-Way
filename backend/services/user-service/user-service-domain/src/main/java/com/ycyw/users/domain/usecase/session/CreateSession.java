package com.ycyw.users.domain.usecase.session;

import com.ycyw.shared.ddd.exceptions.DomainConstraintException;
import com.ycyw.shared.ddd.exceptions.IllegalDomainStateException;
import com.ycyw.shared.ddd.lib.UseCaseHandler;
import com.ycyw.shared.ddd.lib.UseCaseInput;
import com.ycyw.shared.ddd.lib.UseCaseOutput;
import com.ycyw.shared.ddd.objectvalues.JwtAccessToken;
import com.ycyw.users.domain.model.entity.client.Client;
import com.ycyw.users.domain.model.entity.credential.Credential;
import com.ycyw.users.domain.model.entity.credential.CredentialId;
import com.ycyw.users.domain.model.entity.operator.Operator;
import com.ycyw.users.domain.model.valueobject.Email;
import com.ycyw.users.domain.model.valueobject.PasswordCandidate;
import com.ycyw.users.domain.model.valueobject.RawIdentifier;
import com.ycyw.users.domain.model.valueobject.TokenPair;
import com.ycyw.users.domain.model.valueobject.jwt.JwtRefreshToken;
import com.ycyw.users.domain.port.repository.ClientRepository;
import com.ycyw.users.domain.port.repository.CredentialRepository;
import com.ycyw.users.domain.port.repository.OperatorRepository;
import com.ycyw.users.domain.port.service.PasswordHasher;
import com.ycyw.users.domain.service.IdentifierHasher;
import com.ycyw.users.domain.service.SessionService;

import org.eclipse.jdt.annotation.Nullable;

public sealed interface CreateSession {
  String CLIENT_ROLE = "client";
  String OPERATOR_ROLE = "operator";

  record CreateSessionInput(RawIdentifier identifier, PasswordCandidate password)
      implements UseCaseInput, CreateSession {}

  record CreatedSession(JwtAccessToken accessToken, JwtRefreshToken refreshToken)
      implements UseCaseOutput, CreateSession {}

  final class CreateSessionHandler
      implements UseCaseHandler<CreateSessionInput, CreatedSession>, CreateSession {
    private static final String ERR_CREDENTIAL_NOT_FOUND =
        "Credential not found for the given identifier.";
    private static final String ERR_NO_USER_FOR_IDENTIFIER =
        "No user found for the given identifier.";
    private static final String ERR_INVALID_PASSWORD = "Invalid password for the given identifier.";

    private final CredentialRepository credentialRepository;
    private final OperatorRepository operatorRepository;
    private final ClientRepository clientRepository;
    private final SessionService sessionService;
    private final IdentifierHasher identifierHasher;
    private final PasswordHasher passwordHasher;

    public CreateSessionHandler(
        CredentialRepository credentialRepository,
        OperatorRepository operatorRepository,
        ClientRepository clientRepository,
        SessionService sessionService,
        IdentifierHasher identifierHasher,
        PasswordHasher passwordHasher) {
      this.credentialRepository = credentialRepository;
      this.operatorRepository = operatorRepository;
      this.clientRepository = clientRepository;
      this.sessionService = sessionService;
      this.identifierHasher = identifierHasher;
      this.passwordHasher = passwordHasher;
    }

    @Override
    public CreatedSession handle(CreateSessionInput usecaseInput) {
      final RawIdentifier identifier = usecaseInput.identifier();
      final PasswordCandidate password = usecaseInput.password();

      @Nullable final Email identifierAsEmail = getEmailFromRawIdentifier(identifier);

      @Nullable Client client = null;
      @Nullable Operator operator = null;

      if (identifierAsEmail != null) {
        client = clientRepository.findByEmail(identifierAsEmail);
        operator = operatorRepository.findByEmail(identifierAsEmail);
      } else {
        // Non-email path -> hash identifier, resolve credential then user(s)
        final var hashId = identifierHasher.hash(identifier);
        @Nullable final Credential credential = credentialRepository.findByIdentifier(hashId);
        if (credential == null) {
          throw new DomainConstraintException(ERR_CREDENTIAL_NOT_FOUND);
        }
        client = clientRepository.findByCredentialId(credential.getId());
        operator = operatorRepository.findByCredentialId(credential.getId());
      }

      if (client == null && operator == null) {
        throw new DomainConstraintException(ERR_NO_USER_FOR_IDENTIFIER);
      }

      // Validate password against the resolved credential
      final @Nullable CredentialId credentialIdToCheck;
      if (client != null) {
        credentialIdToCheck = client.getCredentialId();
      } else if (operator != null) {
        credentialIdToCheck = operator.getCredentialId();
      } else {
        credentialIdToCheck = null;
      }

      if (credentialIdToCheck == null) {
        throw new DomainConstraintException(ERR_NO_USER_FOR_IDENTIFIER);
      }

      assertPasswordCandidateIsValid(password, credentialIdToCheck);

      if (client != null) {
        return createSessionFor(client.getCredentialId(), CLIENT_ROLE);
      } else if (operator != null) {
        return createSessionFor(operator.getCredentialId(), OPERATOR_ROLE);
      } else {
        throw new DomainConstraintException(ERR_NO_USER_FOR_IDENTIFIER);
      }
    }

    private CreatedSession createSessionFor(CredentialId credentialId, String role) {
      final var authenticableUser = new SessionService.AuthenticableUser(credentialId, role);
      final TokenPair tokenPair = sessionService.generateSessionFor(authenticableUser);
      return new CreatedSession(tokenPair.accessToken(), tokenPair.refreshToken());
    }

    private @Nullable Email getEmailFromRawIdentifier(RawIdentifier identifier) {
      try {
        return new Email(identifier.value());
      } catch (IllegalDomainStateException e) {
        return null;
      }
    }

    private void assertPasswordCandidateIsValid(
        PasswordCandidate password, CredentialId credentialId) {
      @Nullable final Credential credential = credentialRepository.find(credentialId.value());
      if (credential == null) {
        throw new DomainConstraintException(ERR_CREDENTIAL_NOT_FOUND);
      }
      if (!passwordHasher.verify(password, credential.getHashedPassword())) {
        throw new DomainConstraintException(ERR_INVALID_PASSWORD);
      }
    }
  }
}
