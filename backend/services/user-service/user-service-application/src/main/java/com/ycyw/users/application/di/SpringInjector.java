package com.ycyw.users.application.di;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ycyw.users.application.config.AppConfiguration;
import com.ycyw.users.domain.port.repository.ClientRepository;
import com.ycyw.users.domain.port.repository.CredentialRepository;
import com.ycyw.users.domain.port.repository.OperatorRepository;
import com.ycyw.users.domain.port.service.Hasher;
import com.ycyw.users.domain.port.service.PasswordHasher;
import com.ycyw.users.domain.port.service.token.JwtAccessTokenManager;
import com.ycyw.users.domain.port.service.token.JwtRefreshTokenManager;
import com.ycyw.users.domain.service.IdentifierHasher;
import com.ycyw.users.domain.service.SessionService;
import com.ycyw.users.domain.usecase.client.CreateClient;
import com.ycyw.users.domain.usecase.client.FindClient;
import com.ycyw.users.domain.usecase.operator.CreateOperator;
import com.ycyw.users.domain.usecase.operator.FindOperator;
import com.ycyw.users.domain.usecase.session.CreateSession;
import com.ycyw.users.domain.usecase.session.InvalidateSession;
import com.ycyw.users.domain.usecase.session.RefreshSession;
import com.ycyw.users.infrastructure.adapter.service.token.JwtAccessTokenManagerImpl;
import com.ycyw.users.infrastructure.adapter.service.token.JwtRefreshTokenManagerImpl;
import com.ycyw.users.infrastructure.adapter.service.token.JwtTokenProcessorImpl;

import net.datafaker.Faker;

@Configuration
public class SpringInjector {
  // * USECASES
  @Bean
  FindClient.Handler createUser(ClientRepository clientRepository) {
    return new FindClient.Handler(clientRepository);
  }

  @Bean
  CreateClient.Handler createUserHandler(
      CredentialRepository credentialRepository,
      ClientRepository clientRepository,
      IdentifierHasher hasher,
      PasswordHasher passwordHasher) {
    return new CreateClient.Handler(credentialRepository, clientRepository, hasher, passwordHasher);
  }

  @Bean
  FindOperator.Handler createOperator(OperatorRepository operatorRepository) {
    return new FindOperator.Handler(operatorRepository);
  }

  @Bean
  CreateOperator.Handler createOperatorHandler(
      CredentialRepository credentialRepository,
      OperatorRepository operatorRepository,
      IdentifierHasher hasher,
      PasswordHasher passwordHasher) {
    return new CreateOperator.Handler(
        credentialRepository, operatorRepository, hasher, passwordHasher);
  }

  @Bean
  CreateSession.Handler createSessionHandler(
      CredentialRepository credentialRepository,
      OperatorRepository operatorRepository,
      ClientRepository clientRepository,
      SessionService sessionService,
      IdentifierHasher identifierHasher,
      PasswordHasher passwordHasher) {
    return new CreateSession.Handler(
        credentialRepository,
        operatorRepository,
        clientRepository,
        sessionService,
        identifierHasher,
        passwordHasher);
  }

  @Bean
  InvalidateSession.Handler invalidateSessionHandler(
      CredentialRepository credentialRepository, SessionService sessionService) {
    return new InvalidateSession.Handler(credentialRepository, sessionService);
  }

  @Bean
  RefreshSession.Handler refreshSessionHandler(
      CredentialRepository credentialRepository, SessionService sessionService) {
    return new RefreshSession.Handler(credentialRepository, sessionService);
  }

  // * OTHER DOMAIN SERVICES
  @Bean
  SessionService sessionService(
      JwtAccessTokenManager accessTokenManager, JwtRefreshTokenManager refreshTokenManager) {
    return new SessionService(accessTokenManager, refreshTokenManager);
  }

  @Bean
  IdentifierHasher identifierHasher(Hasher hasher) {
    return new IdentifierHasher(hasher);
  }

  @Bean
  JwtAccessTokenManager jwtAccessTokenManager(AppConfiguration appConfiguration) {
    final var appName = appConfiguration.getAppName();
    final var jwtSecretKey = appConfiguration.getJwtSecretKey();
    final var jwtTokenExpiration = appConfiguration.getJwtTokenExpiration();
    final var jwtTokenProcessor =
        new JwtTokenProcessorImpl(jwtTokenExpiration, jwtSecretKey, appName);
    return new JwtAccessTokenManagerImpl(jwtTokenProcessor);
  }

  @Bean
  JwtRefreshTokenManager jwtRefreshTokenManager(AppConfiguration appConfiguration) {
    final var appName = appConfiguration.getAppName();
    final var jwtSecretKey = appConfiguration.getJwtSecretKey();
    final var jwtRefreshExpiration = appConfiguration.getJwtRefreshExpiration();
    final var jwtTokenProcessor =
        new JwtTokenProcessorImpl(jwtRefreshExpiration, jwtSecretKey, appName);
    return new JwtRefreshTokenManagerImpl(jwtTokenProcessor);
  }

  // * MISCELLANEOUS
  @Bean
  Faker dataFaker() {
    return new Faker();
  }
}
