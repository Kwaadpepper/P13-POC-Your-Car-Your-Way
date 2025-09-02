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
import com.ycyw.users.domain.model.valueobject.TokenPair;
import com.ycyw.users.domain.model.valueobject.jwt.JwtRefreshToken;
import com.ycyw.users.domain.usecase.session.RefreshSession;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class RefreshController {
  private final UseCaseExecutor useCaseExecutor;
  private final RefreshSession.Handler refreshSessionHandler;
  private final CookieService cookieService;
  private static final Logger logger = LoggerFactory.getLogger(RefreshController.class);

  public RefreshController(
      UseCaseExecutor useCaseExecutor,
      RefreshSession.Handler refreshSessionHandler,
      CookieService cookieService) {
    this.useCaseExecutor = useCaseExecutor;
    this.refreshSessionHandler = refreshSessionHandler;
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
  @PostMapping(value = "/auth/refresh-token", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<SimpleMessageDto> login(HttpServletRequest request) {

    final var jwtAccessToken = cookieService.getJwtAccessTokenFromRequest(request);
    if (jwtAccessToken == null) {
      logger.debug("No access token found in request");
      return ResponseEntity.badRequest().body(new SimpleMessageDto("No access token"));
    }
    final var jwtRefreshToken = cookieService.getJwtRefreshTokenFromRequest(request);
    if (jwtRefreshToken == null) {
      logger.debug("No refresh token found in request");
      return ResponseEntity.badRequest().body(new SimpleMessageDto("No refresh token"));
    }

    final var refreshedSession = refreshSession(jwtAccessToken, jwtRefreshToken);
    final var tokenPair = refreshedSession.tokenPair();

    final List<ResponseCookie> cookieList =
        List.of(cookieService.generateJwtCookie(tokenPair.accessToken()));
    final var response = ResponseEntity.ok();

    cookieList.forEach(cookie -> response.header(HttpHeaders.SET_COOKIE, cookie.toString()));

    return response.body(new SimpleMessageDto("refreshed"));
  }

  private RefreshSession.NewTokens refreshSession(
      final JwtAccessToken accessToken, final JwtRefreshToken refreshToken) {
    final var input = new RefreshSession.OldTokens(new TokenPair(accessToken, refreshToken));
    return this.useCaseExecutor.execute(this.refreshSessionHandler, input);
  }
}
