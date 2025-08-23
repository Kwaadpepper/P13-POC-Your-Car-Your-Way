package com.ycyw.users.application.route.auth;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ycyw.shared.ddd.lib.UseCaseExecutor;
import com.ycyw.shared.ddd.objectvalues.JwtAccessToken;
import com.ycyw.users.application.dto.VerifiedSessionDto;
import com.ycyw.users.application.exception.exceptions.BadRequestException;
import com.ycyw.users.application.presenter.VerifiedSessionPresenter;
import com.ycyw.users.application.service.CookieService;
import com.ycyw.users.domain.usecase.session.VerifySession;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class VerifyController {
  private static final Logger logger = LoggerFactory.getLogger(VerifyController.class);
  private final UseCaseExecutor useCaseExecutor;
  private final VerifySession.Handler refreshSessionHandler;
  private final VerifiedSessionPresenter presenter;
  private final CookieService cookieService;

  public VerifyController(
      UseCaseExecutor useCaseExecutor,
      VerifySession.Handler refreshSessionHandler,
      VerifiedSessionPresenter presenter,
      CookieService cookieService) {
    this.useCaseExecutor = useCaseExecutor;
    this.refreshSessionHandler = refreshSessionHandler;
    this.presenter = presenter;
    this.cookieService = cookieService;
  }

  @PostMapping(value = "/auth/verify", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<VerifiedSessionDto> login(HttpServletRequest request) {

    final var jwtAccessToken = cookieService.getJwtAccessTokenFromRequest(request);
    if (jwtAccessToken == null) {
      logger.debug("No access token found in request");
      throw new BadRequestException("No access token found in request");
    }

    final var output = verifySession(jwtAccessToken);
    final var response = ResponseEntity.ok();

    return response.body(presenter.present(output));
  }

  private VerifySession.Output verifySession(final JwtAccessToken accessToken) {
    final var input = new VerifySession.AccessToken(accessToken);
    return this.useCaseExecutor.execute(this.refreshSessionHandler, input);
  }
}
