package com.ycyw.users.domain.usecase.session;

import com.ycyw.shared.ddd.exceptions.DomainConstraintException;
import com.ycyw.shared.ddd.lib.UseCaseHandler;
import com.ycyw.shared.ddd.lib.UseCaseInput;
import com.ycyw.shared.ddd.lib.UseCaseOutput;
import com.ycyw.users.domain.model.valueobject.TokenPair;
import com.ycyw.users.domain.model.valueobject.jwt.RefreshTokenSubject;
import com.ycyw.users.domain.port.repository.CredentialRepository;
import com.ycyw.users.domain.service.SessionService;

import org.eclipse.jdt.annotation.Nullable;

public sealed interface RefreshSession {

  record OldTokens(TokenPair tokenPair) implements UseCaseInput, RefreshSession {}

  record NewTokens(TokenPair tokenPair) implements UseCaseOutput, RefreshSession {}

  final class Handler implements UseCaseHandler<OldTokens, NewTokens>, RefreshSession {

    private final CredentialRepository credentialRepository;
    private final SessionService sessionService;

    public Handler(CredentialRepository credentialRepository, SessionService sessionService) {
      this.credentialRepository = credentialRepository;
      this.sessionService = sessionService;
    }

    @Override
    public NewTokens handle(OldTokens usecaseInput) {
      final var tokenPair = usecaseInput.tokenPair();

      @Nullable RefreshTokenSubject refreshTokenSubject = sessionService.verify(tokenPair.refreshToken());

      if (refreshTokenSubject == null) {
        throw new DomainConstraintException("The provided refresh token is invalid.");
      }

      if (credentialRepository.find(refreshTokenSubject.value()) == null) {
        throw new DomainConstraintException("No user account found for the token pair");
      }

      final @Nullable TokenPair refreshedTokenPair = sessionService.refresh(tokenPair);

      if (refreshedTokenPair == null) {
        throw new DomainConstraintException("Failed to refresh the session tokens");
      }

      return new NewTokens(refreshedTokenPair);
    }
  }
}
