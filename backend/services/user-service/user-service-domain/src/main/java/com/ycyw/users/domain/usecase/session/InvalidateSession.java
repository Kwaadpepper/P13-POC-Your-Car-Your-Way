package com.ycyw.users.domain.usecase.session;

import com.ycyw.shared.ddd.exceptions.DomainConstraintException;
import com.ycyw.shared.ddd.lib.UseCaseHandler;
import com.ycyw.shared.ddd.lib.UseCaseInput;
import com.ycyw.shared.ddd.lib.UseCaseOutput;
import com.ycyw.shared.ddd.objectvalues.JwtAccessToken;
import com.ycyw.users.domain.model.valueobject.TokenPair;
import com.ycyw.users.domain.model.valueobject.jwt.AccessTokenSubject;
import com.ycyw.users.domain.model.valueobject.jwt.JwtRefreshToken;
import com.ycyw.users.domain.port.repository.CredentialRepository;
import com.ycyw.users.domain.service.SessionService;

import org.eclipse.jdt.annotation.Nullable;

public sealed interface InvalidateSession {

  record InvalidateSessionInput(JwtAccessToken accessToken)
      implements UseCaseInput, InvalidateSession {}

  record InvalidatedSession() implements UseCaseOutput, InvalidateSession {}

  final class InvalidateSessionHandler
      implements UseCaseHandler<InvalidateSessionInput, InvalidatedSession>, InvalidateSession {

    private final CredentialRepository credentialRepository;
    private final SessionService sessionService;

    public InvalidateSessionHandler(
        CredentialRepository credentialRepository, SessionService sessionService) {
      this.credentialRepository = credentialRepository;
      this.sessionService = sessionService;
    }

    @Override
    public InvalidatedSession handle(InvalidateSessionInput usecaseInput) {
      final var accessToken = usecaseInput.accessToken();

      final @Nullable JwtRefreshToken refreshToken =
          sessionService.findRefreshTokenFor(accessToken);

      if (refreshToken == null) {
        throw new DomainConstraintException(
            "No refresh token found for the provided access token.");
      }
      final var tokenPair = new TokenPair(accessToken, refreshToken);

      final @Nullable AccessTokenSubject login = sessionService.verify(tokenPair);
      if (login == null) {
        throw new DomainConstraintException("Access token is invalid or could not be extracted.");
      }

      if (credentialRepository.find(login.value()) == null) {
        throw new DomainConstraintException("Cannot invalidate session for a non-existent user.");
      }

      sessionService.invalidate(tokenPair);

      return new InvalidatedSession();
    }
  }
}
