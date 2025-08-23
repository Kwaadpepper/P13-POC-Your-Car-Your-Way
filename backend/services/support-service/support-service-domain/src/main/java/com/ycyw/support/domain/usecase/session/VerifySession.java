package com.ycyw.support.domain.usecase.session;

import java.util.UUID;

import com.ycyw.shared.ddd.lib.UseCaseHandler;
import com.ycyw.shared.ddd.lib.UseCaseInput;
import com.ycyw.shared.ddd.lib.UseCaseOutput;
import com.ycyw.shared.ddd.objectvalues.JwtAccessToken;
import com.ycyw.support.domain.port.service.SessionVerifyer;
import com.ycyw.support.domain.port.service.SessionVerifyer.UserDetails;

import org.eclipse.jdt.annotation.Nullable;

public sealed interface VerifySession {
  public record AccessToken(JwtAccessToken accessToken) implements UseCaseInput {}

  public sealed interface Output extends UseCaseOutput {
    record SessionVerified(UUID subject, String username, String role) implements Output {}

    record SessionInvalid() implements Output {}
  }

  public final class Handler implements UseCaseHandler<AccessToken, Output>, VerifySession {
    private final SessionVerifyer sessionVerifyer;

    public Handler(SessionVerifyer sessionVerifyer) {
      this.sessionVerifyer = sessionVerifyer;
    }

    @Override
    public Output handle(AccessToken input) {

      @Nullable final UserDetails session = this.sessionVerifyer.verify(input.accessToken());

      if (session == null) {
        return new Output.SessionInvalid();
      }

      return new Output.SessionVerified(session.subject(), session.username(), session.role());
    }
  }
}
