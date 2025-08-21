package com.ycyw.users.application.route.auth;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;

import com.ycyw.users.application.dto.SimpleMessageDto;

public class LogoutController {
  @PostMapping(value = "/auth/logout", produces = MediaType.APPLICATION_JSON_VALUE)
  public SimpleMessageDto logoutUser() {
    // final var user = sessionService.getAuthenticatedUser().or(() -> {
    //     throw new JwtAuthenticationFailureException("No user is authenticated.");
    // }).get();

    // final var jwtCookieList = sessionService.removeSessionForUser(user);
    // final var message = new SimpleMessage("Logged out!");
    // final var response = ResponseEntity.ok();

    // jwtCookieList.forEach(cookie -> response.header(HttpHeaders.SET_COOKIE, cookie.toString()));

    // return response.body(message);
    return new SimpleMessageDto("Logged out!");
  }
}
