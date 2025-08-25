package com.ycyw.support.application.component;

// import java.util.Map;

// import org.springframework.http.server.ServerHttpRequest;
// import org.springframework.http.server.ServerHttpResponse;
// import org.springframework.security.authentication.BadCredentialsException;
// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.security.core.context.SecurityContext;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
// import org.springframework.web.socket.WebSocketHandler;
// import org.springframework.web.socket.server.HandshakeInterceptor;

// import com.ycyw.shared.ddd.objectvalues.JwtAccessToken;
// import com.ycyw.support.application.exception.exceptions.JwtAuthenticationFailureException;
// import com.ycyw.support.application.service.AuthenticationService;
// import com.ycyw.support.application.service.CookieService;

// import org.eclipse.jdt.annotation.Nullable;

// public class JwtHandshakeInterceptor implements HandshakeInterceptor {
//   private final AuthenticationService authenticationService;
//   private final CookieService cookieService;

//   public JwtHandshakeInterceptor(
//       AuthenticationService authenticationService, CookieService cookieService) {
//     this.authenticationService = authenticationService;
//     this.cookieService = cookieService;
//   }

//   @Override
//   public boolean beforeHandshake(
//       ServerHttpRequest request,
//       ServerHttpResponse response,
//       WebSocketHandler wsHandler,
//       Map<String, Object> attributes) {

//     final var jwtToken = cookieService.getJwtAccessTokenFromHttpRequest(request);

//     if (jwtToken == null) {
//       return false;
//     }

//     @Nullable final UserDetails userDetails = getUserDetailsFromJwtToken(jwtToken);

//     if (userDetails == null) {
//       throw new JwtAuthenticationFailureException(
//           "JwtToken does not contain a valid user or has expired.");
//     }

//     final SecurityContext securityContext = getNewSecurityContext(request, userDetails,
// jwtToken);
//     SecurityContextHolder.setContext(securityContext);

//     // Stocke le principal
//     attributes.put("user", user);
//     return true;
//   }

//   @Override
//   public void afterHandshake(
//       ServerHttpRequest request,
//       ServerHttpResponse response,
//       WebSocketHandler wsHandler,
//       Exception exception) {}

//   private SecurityContext getNewSecurityContext(
//       ServerHttpRequest request, UserDetails userDetails, JwtAccessToken accessToken) {
//     final var context = SecurityContextHolder.createEmptyContext();
//     final var authToken =
//         new UsernamePasswordAuthenticationToken(
//             userDetails, accessToken, userDetails.getAuthorities());

//     authToken.setDetails(new
// WebAuthenticationDetailsSource().buildDetails(request.getPrincipal()));

//     context.setAuthentication(authToken);
//     return context;
//   }

//   private @Nullable UserDetails getUserDetailsFromJwtToken(JwtAccessToken accessToken) {
//     try {
//       return authenticationService.authenticate(accessToken);
//     } catch (final BadCredentialsException e) {
//       return null;
//     }
//   }
// }
