package com.ycyw.users.domain.usecase.session;

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

  record SessionVerified(AccessTokenClaims claims) implements UseCaseOutput, VerifySession {}

  final class Handler implements UseCaseHandler<AccessToken, SessionVerified>, VerifySession {

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
    public SessionVerified handle(AccessToken usecaseInput) {
      final var token = usecaseInput.accessToken();
      final @Nullable JwtRefreshToken refreshToken = sessionService.findRefreshTokenFor(token);

      if (refreshToken == null) {
        throw new DomainConstraintException(
            "No refresh token found for the provided access token.");
      }

      var tokenPair = new TokenPair(token, refreshToken);

      @Nullable AccessTokenSubject subject = sessionService.verify(tokenPair);
      if (subject == null) {
        throw new DomainConstraintException("Access token is invalid or could not be extracted.");
      }

      @Nullable Client client = clientRepository.findByCredentialId(subject.value());
      @Nullable Operator operator = operatorRepository.findByCredentialId(subject.value());

      if (client == null && operator == null) {
        throw new DomainConstraintException("No user account found for the access token.");
      }
      if (client != null && operator != null) {
        throw new IllegalDomainStateException(
            "Access token belongs to both a client and an operator.");
      }

      @Nullable String role = sessionService.getRole(tokenPair);
      if (role == null) {
        throw new DomainConstraintException("Role could not be determined from the access token.");
      }

      return new SessionVerified(new AccessTokenClaims(subject, role));
    }
  }
}
