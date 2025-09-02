package com.ycyw.users.application.route.auth;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ycyw.shared.ddd.exceptions.DomainConstraintException;
import com.ycyw.shared.ddd.lib.UseCaseExecutor;
import com.ycyw.users.application.dto.AuthenticableViewDto;
import com.ycyw.users.application.exception.exceptions.AuthenticationFailureException;
import com.ycyw.users.application.exception.exceptions.BadRequestException;
import com.ycyw.users.application.request.LoginRequest;
import com.ycyw.users.application.service.CookieService;
import com.ycyw.users.domain.model.valueobject.PasswordCandidate;
import com.ycyw.users.domain.model.valueobject.RawIdentifier;
import com.ycyw.users.domain.usecase.session.CreateSession;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class LoginController {
  private final UseCaseExecutor useCaseExecutor;
  private final CreateSession.Handler createSessionHandler;
  private final CookieService cookieService;
  private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

  public LoginController(
      UseCaseExecutor useCaseExecutor,
      CreateSession.Handler createSessionHandler,
      CookieService cookieService) {
    this.useCaseExecutor = useCaseExecutor;
    this.createSessionHandler = createSessionHandler;
    this.cookieService = cookieService;
  }

  @PostMapping(
      value = "/auth/login",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<AuthenticableViewDto> login(
      @Valid @RequestBody final LoginRequest request) {

    final var login = toId(request.login());
    final var password = toPassword(request.password());
    final var session = createSession(login, password);

    final List<ResponseCookie> cookieList =
        List.of(
            cookieService.generateJwtCookie(session.accessToken()),
            cookieService.generateRefreshJwtCookie(session.refreshToken()));
    final var response = ResponseEntity.ok();

    cookieList.forEach(cookie -> response.header(HttpHeaders.SET_COOKIE, cookie.toString()));

    return response.body(toAuthenticableViewDto(session));
  }

  private CreateSession.NewSession createSession(
      final RawIdentifier login, final PasswordCandidate password) {
    try {
      return this.useCaseExecutor.execute(
          this.createSessionHandler, new CreateSession.Credentials(login, password));
    } catch (DomainConstraintException e) {
      logger.debug("Domain exception while creating sessions: {}", e.getMessage());
      throw new AuthenticationFailureException("Login failure " + e.getMessage());
    }
  }

  private AuthenticableViewDto toAuthenticableViewDto(CreateSession.NewSession session) {
    return new AuthenticableViewDto(
        session.id(),
        session.name(),
        session.role(),
        session.email().value(),
        session.createdAt(),
        session.updatedAt());
  }

  private RawIdentifier toId(String login) {
    try {
      return new RawIdentifier(login);
    } catch (IllegalArgumentException e) {
      logger.debug("Invalid login format");
      throw new BadRequestException("Invalid login");
    }
  }

  private PasswordCandidate toPassword(String password) {
    try {
      return new PasswordCandidate(password);
    } catch (IllegalArgumentException e) {
      logger.debug("Invalid password format");
      throw new BadRequestException("Invalid password format");
    }
  }
}
