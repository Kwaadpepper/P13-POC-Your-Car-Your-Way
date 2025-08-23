package com.ycyw.users.application.component;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.ycyw.shared.ddd.objectvalues.JwtAccessToken;
import com.ycyw.users.application.exception.exceptions.JwtAuthenticationFailureException;
import com.ycyw.users.application.service.AuthenticationService;
import com.ycyw.users.application.service.CookieService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.jdt.annotation.Nullable;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  final AuthenticationService authenticationService;
  private final CookieService cookieService;
  private final HandlerExceptionResolver exceptionResolver;
  private List<String> ignoreUrls;

  public JwtAuthenticationFilter(
      AuthenticationService authenticationService,
      CookieService cookieService,
      @Qualifier("handlerExceptionResolver") HandlerExceptionResolver exceptionResolver) {
    this.authenticationService = authenticationService;
    this.cookieService = cookieService;
    this.exceptionResolver = exceptionResolver;
    this.ignoreUrls = new ArrayList<>();
  }

  public void setIgnoreUrls(List<String> ignoreUrls) {
    this.ignoreUrls = ignoreUrls;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse servletResponse, FilterChain filterChain)
      throws ServletException, IOException {
    try {
      final var jwtToken = cookieService.getJwtAccessTokenFromRequest(request);

      if (jwtToken == null || !urlHasToBeFiltered(request)) {
        logger.debug("Request does not have 'Authorize' header, skipping authentication.");
        filterChain.doFilter(request, servletResponse);
        return;
      }

      @Nullable final UserDetails userDetails = getUserDetailsFromJwtToken(jwtToken);

      if (userDetails == null) {
        throw new JwtAuthenticationFailureException(
            "JwtToken does not contain a valid user or has expired.");
      }

      final SecurityContext securityContext = getNewSecurityContext(request, userDetails, jwtToken);
      SecurityContextHolder.setContext(securityContext);

      // Pursue the filter chain.
      filterChain.doFilter(request, servletResponse);
    } catch (final Exception e) {
      // Dispatch the exception to our Global handler.
      exceptionResolver.resolveException(request, servletResponse, null, e);
    }
  }

  private boolean urlHasToBeFiltered(HttpServletRequest request) {
    try {
      final var url = URI.create(request.getRequestURI());
      return ignoreUrls.stream().filter(ignoreUrl -> url.getPath().startsWith(ignoreUrl)).count()
          == 0;
    } catch (NullPointerException | IllegalArgumentException e) {
      return false;
    }
  }

  private SecurityContext getNewSecurityContext(
      HttpServletRequest request, UserDetails userDetails, JwtAccessToken accessToken) {
    final var context = SecurityContextHolder.createEmptyContext();
    final var authToken =
        new UsernamePasswordAuthenticationToken(
            userDetails, accessToken, userDetails.getAuthorities());

    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

    context.setAuthentication(authToken);
    return context;
  }

  private @Nullable UserDetails getUserDetailsFromJwtToken(JwtAccessToken accessToken) {
    try {
      return authenticationService.authenticate(accessToken);
    } catch (final BadCredentialsException e) {
      return null;
    }
  }
}
