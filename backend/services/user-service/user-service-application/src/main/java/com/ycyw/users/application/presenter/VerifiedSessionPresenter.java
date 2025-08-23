package com.ycyw.users.application.presenter;

import org.springframework.stereotype.Component;

import com.ycyw.shared.application.Presenter;
import com.ycyw.users.application.dto.VerifiedSessionDto;
import com.ycyw.users.application.exception.exceptions.UnauthorizedException;
import com.ycyw.users.domain.usecase.session.VerifySession;
import com.ycyw.users.domain.usecase.session.VerifySession.Output;

@Component
public class VerifiedSessionPresenter
    implements Presenter<VerifiedSessionDto, VerifySession.Output> {

  @Override
  public VerifiedSessionDto present(Output model) {
    return switch (model) {
      case VerifySession.Output.SessionInvalid() ->
          throw new UnauthorizedException("Invalid session");
      case VerifySession.Output.SessionVerified(var claims, var additionals) ->
          new VerifiedSessionDto(claims.subject().value(), additionals.username(), claims.role());
    };
  }
}
