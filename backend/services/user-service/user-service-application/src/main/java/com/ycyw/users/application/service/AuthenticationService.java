package com.ycyw.users.application.service;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.ycyw.shared.ddd.exceptions.DomainConstraintException;
import com.ycyw.shared.ddd.lib.UseCaseExecutor;
import com.ycyw.shared.ddd.objectvalues.JwtAccessToken;
import com.ycyw.users.application.exception.exceptions.ServerErrorException;
import com.ycyw.users.domain.usecase.session.VerifySession;

import jakarta.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Service
public class AuthenticationService {
  private static final Logger logger = LogManager.getLogger(AuthenticationService.class);
  private final UseCaseExecutor useCaseExecutor;
  private final VerifySession.Handler verifySessionHandler;

  public AuthenticationService(
      final UseCaseExecutor useCaseExecutor, final VerifySession.Handler verifySessionHandler) {
    this.useCaseExecutor = useCaseExecutor;
    this.verifySessionHandler = verifySessionHandler;
  }

  public @Nullable AuthenticatedUser getAuthenticated() {
    var securityContext = SecurityContextHolder.getContext();
    @Nullable Authentication authentication = securityContext.getAuthentication();

    if (authentication == null) {
      return null;
    }

    return toUser(authentication);
  }

  public AuthenticatedUser authenticate(final JwtAccessToken accessToken)
      throws BadCredentialsException {
    try {
      final var input = new VerifySession.AccessToken(accessToken);
      final var output = useCaseExecutor.execute(verifySessionHandler, input);

      return switch (output) {
        case VerifySession.Output.SessionInvalid ignored ->
            throw new BadCredentialsException("Account cannot be used for the moment");
        case VerifySession.Output.SessionVerified(var claims, var additionals) ->
            new AuthenticatedUser(
                claims.subject().value(), additionals.username(), accessToken, claims.role());
      };
    } catch (DomainConstraintException e) {
      throw new BadCredentialsException("Account cannot be used for the moment");
    }
  }

  private AuthenticatedUser toUser(final Authentication authentication)
      throws ServerErrorException {
    final var principal = authentication.getPrincipal();

    if (principal instanceof AuthenticatedUser authenticatedUser) {
      return authenticatedUser;
    }

    logger.debug("Given authentication principal is not a AuthenticatedUser instance.");
    throw new ServerErrorException(
        "Expected principal to be a '%s' instance given is '%s'"
            .formatted(AuthenticatedUser.class, principal.getClass()));
  }

  public record AuthenticatedUser(
      UUID id, String username, JwtAccessToken jwtAccessToken, String role) implements UserDetails {
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
      return List.of();
    }

    @Override
    public String getPassword() {
      return jwtAccessToken.value();
    }

    @Override
    public String getUsername() {
      return username;
    }
  }
}
