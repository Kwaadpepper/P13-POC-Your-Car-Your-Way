package com.ycyw.users.application.route.auth;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ycyw.shared.ddd.lib.UseCaseExecutor;
import com.ycyw.shared.ddd.objectvalues.JwtAccessToken;
import com.ycyw.users.application.dto.ApiErrorDetails;
import com.ycyw.users.application.dto.SimpleMessageDto;
import com.ycyw.users.application.service.CookieService;
import com.ycyw.users.domain.usecase.session.InvalidateSession;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class LogoutController {
  private final UseCaseExecutor useCaseExecutor;
  private final InvalidateSession.Handler invalidateSessionHandler;
  private final CookieService cookieService;
  private static final Logger logger = LoggerFactory.getLogger(LogoutController.class);

  public LogoutController(
      UseCaseExecutor useCaseExecutor,
      InvalidateSession.Handler invalidateSessionHandler,
      CookieService cookieService) {
    this.useCaseExecutor = useCaseExecutor;
    this.invalidateSessionHandler = invalidateSessionHandler;
    this.cookieService = cookieService;
  }

  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            content = @Content(schema = @Schema(implementation = SimpleMessageDto.class))),
        @ApiResponse(
            responseCode = "400",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ApiErrorDetails.class)))
      })
  @SecurityRequirements
  @PostMapping(value = "/auth/logout", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<SimpleMessageDto> logout(HttpServletRequest request) {

    final var jwtAccessToken = cookieService.getJwtAccessTokenFromRequest(request);
    if (jwtAccessToken == null) {
      logger.debug("No access token found in request");
      return ResponseEntity.badRequest().body(new SimpleMessageDto("No access token"));
    }

    invalidateSession(jwtAccessToken);

    final List<ResponseCookie> cookieList =
        List.of(
            cookieService.generateCookieRemoval(), cookieService.generateRefreshJwtCookieRemoval());
    final var response = ResponseEntity.ok();

    cookieList.forEach(cookie -> response.header(HttpHeaders.SET_COOKIE, cookie.toString()));

    return response.body(new SimpleMessageDto("success"));
  }

  private InvalidateSession.SessionInvalidated invalidateSession(JwtAccessToken accessToken) {
    final var input = new InvalidateSession.AccessToken(accessToken);
    return this.useCaseExecutor.execute(invalidateSessionHandler, input);
  }
}
