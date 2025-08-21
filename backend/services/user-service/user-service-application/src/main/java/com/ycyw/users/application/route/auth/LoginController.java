package com.ycyw.users.application.route.auth;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ycyw.users.application.dto.AuthenticableViewDto;
import com.ycyw.users.application.request.LoginRequest;

import jakarta.validation.Valid;

@RestController
public class LoginController {
  @PostMapping(
      value = "/auth/login",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public AuthenticableViewDto login(@Valid @RequestBody final LoginRequest request) {

    // final var credentials = authenticationService.authenticate(request.login(),
    // request.password());
    // final var user = credentials.getUser();
    // final var cookieList = sessionService.createSessionFor(credentials);
    // final var response = ResponseEntity.ok();

    // cookieList.forEach(cookie -> response.header(HttpHeaders.SET_COOKIE, cookie.toString()));

    // return response.body(userPresenter.present(user));
    return new AuthenticableViewDto(
        UUID.randomUUID(),
        "name",
        "user@example.net",
        ZonedDateTime.now(ZoneId.systemDefault()),
        ZonedDateTime.now(ZoneId.systemDefault()));
  }
}
