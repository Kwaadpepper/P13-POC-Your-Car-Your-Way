package com.ycyw.users.domain.usecase.session;

import java.util.Objects;

import com.ycyw.shared.ddd.exceptions.DomainConstraintException;
import com.ycyw.shared.ddd.exceptions.IllegalDomainStateException;
import com.ycyw.shared.ddd.lib.UseCaseHandler;
import com.ycyw.shared.ddd.lib.UseCaseInput;
import com.ycyw.shared.ddd.lib.UseCaseOutput;
import com.ycyw.shared.ddd.objectvalues.JwtAccessToken;
import com.ycyw.users.domain.model.entity.client.Client;
import com.ycyw.users.domain.model.entity.operator.Operator;
import com.ycyw.users.domain.model.valueobject.TokenPair;
import com.ycyw.users.domain.model.valueobject.jwt.AccessTokenClaims;
import com.ycyw.users.domain.model.valueobject.jwt.AccessTokenSubject;
import com.ycyw.users.domain.model.valueobject.jwt.JwtRefreshToken;
import com.ycyw.users.domain.port.repository.ClientRepository;
import com.ycyw.users.domain.port.repository.OperatorRepository;
import com.ycyw.users.domain.service.SessionService;

import org.eclipse.jdt.annotation.Nullable;

public sealed interface VerifySession {

  record AccessToken(JwtAccessToken accessToken) implements UseCaseInput, VerifySession {}

  sealed interface Output extends UseCaseOutput, VerifySession {
    record SessionVerified(AccessTokenClaims claims, Additionals additionals) implements Output {
      public record Additionals(String username) {}
    }

    record SessionInvalid() implements Output {}
  }

  final class Handler implements UseCaseHandler<AccessToken, Output>, VerifySession {
    private final ClientRepository clientRepository;
    private final OperatorRepository operatorRepository;
    private final SessionService sessionService;

    public Handler(
        ClientRepository clientRepository,
        OperatorRepository operatorRepository,
        SessionService sessionService) {
      this.clientRepository = clientRepository;
      this.operatorRepository = operatorRepository;
      this.sessionService = sessionService;
    }

    @Override
    public Output handle(AccessToken usecaseInput) {
      final var token = usecaseInput.accessToken();
      final @Nullable JwtRefreshToken refreshToken = sessionService.findRefreshTokenFor(token);

      if (refreshToken == null) {
        throw new DomainConstraintException(
            "No refresh token found for the provided access token.");
      }

      final var tokenPair = new TokenPair(token, refreshToken);
      final var subject = getSubject(tokenPair);

      @Nullable Client client = clientRepository.findByCredentialId(subject.value());
      @Nullable Operator operator = operatorRepository.findByCredentialId(subject.value());

      if (client == null && operator == null) {
        return new Output.SessionInvalid();
      }
      if (client != null && operator != null) {
        throw new IllegalDomainStateException(
            "Access token belongs to both a client and an operator.");
      }

      final var additionals =
          new Output.SessionVerified.Additionals(
              client != null
                  ? (client.getLastName() + client.getFirstName())
                  : Objects.requireNonNull(operator).getName());
      final var role = getRole(tokenPair);

      return new Output.SessionVerified(new AccessTokenClaims(subject, role), additionals);
    }

    private AccessTokenSubject getSubject(TokenPair tokenPair) {
      AccessTokenSubject subject = sessionService.verify(tokenPair);

      if (subject == null) {
        throw new DomainConstraintException(
            "Subject could not be extracted from the access token.");
      }

      return subject;
    }

    private String getRole(TokenPair tokenPair) {
      String role = sessionService.getRole(tokenPair);

      if (role == null) {
        throw new DomainConstraintException("Role could not be determined from the access token.");
      }

      return role;
    }
  }
}
