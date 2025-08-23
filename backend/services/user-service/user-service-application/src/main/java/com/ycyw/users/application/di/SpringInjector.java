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
import com.ycyw.users.domain.usecase.session.VerifySession;
import com.ycyw.users.infrastructure.adapter.service.token.JwtAccessTokenManagerImpl;
import com.ycyw.users.infrastructure.adapter.service.token.JwtRefreshTokenManagerImpl;
import com.ycyw.users.infrastructure.adapter.service.token.JwtTokenProcessorImpl;
import com.ycyw.users.infrastructure.storage.KeyStorage;

import net.datafaker.Faker;

@Configuration
public class SpringInjector {
  private final AppConfiguration appConfiguration;

  public SpringInjector(AppConfiguration appConfiguration) {
    this.appConfiguration = appConfiguration;
  }

  // * USECASES
  @Bean
  FindClient.Handler createUser(ClientRepository clientRepository) {
    return new FindClient.Handler(clientRepository);
  }

  @Bean
  CreateClient.Handler createUserHandler(
      IdentifierHasher identifierHasher,
      PasswordHasher passwordHasher,
      ClientRepository clientRepository,
      CredentialRepository credentialRepository) {
    return new CreateClient.Handler(
        credentialRepository, clientRepository, identifierHasher, passwordHasher);
  }

  @Bean
  FindOperator.Handler createOperator(OperatorRepository operatorRepository) {
    return new FindOperator.Handler(operatorRepository);
  }

  @Bean
  CreateOperator.Handler createOperatorHandler(
      IdentifierHasher identifierHasher,
      PasswordHasher passwordHasher,
      OperatorRepository operatorRepository,
      CredentialRepository credentialRepository) {
    return new CreateOperator.Handler(
        credentialRepository, operatorRepository, identifierHasher, passwordHasher);
  }

  @Bean
  CreateSession.Handler createSessionHandler(
      IdentifierHasher identifierHasher,
      PasswordHasher passwordHasher,
      OperatorRepository operatorRepository,
      ClientRepository clientRepository,
      CredentialRepository credentialRepository,
      SessionService sessionService) {
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

  @Bean
  VerifySession.Handler verifySessionHandler(
      OperatorRepository operatorRepository,
      ClientRepository clientRepository,
      SessionService sessionService) {
    return new VerifySession.Handler(clientRepository, operatorRepository, sessionService);
  }

  // * OTHER DOMAIN SERVICES
  @Bean
  SessionService sessionService(
      JwtAccessTokenManager jwtAccessTokenManager, JwtRefreshTokenManager jwtRefreshTokenManager) {
    return new SessionService(jwtAccessTokenManager, jwtRefreshTokenManager);
  }

  @Bean
  IdentifierHasher identifierHasher(Hasher hasher) {
    return new IdentifierHasher(hasher);
  }

  @Bean
  JwtAccessTokenManager jwtAccessTokenManager(KeyStorage keyStorage) {
    final var jwtIssuer = appConfiguration.getJwtIssuer();
    final var jwtSecretKey = appConfiguration.getJwtSecretKey();
    final var jwtTokenExpiration = appConfiguration.getJwtTokenExpiration();
    final var jwtTokenProcessor =
        new JwtTokenProcessorImpl(jwtTokenExpiration, jwtSecretKey, jwtIssuer);

    return new JwtAccessTokenManagerImpl(jwtTokenProcessor, keyStorage);
  }

  @Bean
  JwtRefreshTokenManager jwtRefreshTokenManager(KeyStorage keyStorage) {
    final var jwtIssuer = appConfiguration.getJwtIssuer();
    final var jwtSecretKey = appConfiguration.getJwtSecretKey();
    final var jwtRefreshExpiration = appConfiguration.getJwtRefreshExpiration();
    final var jwtTokenProcessor =
        new JwtTokenProcessorImpl(jwtRefreshExpiration, jwtSecretKey, jwtIssuer);

    return new JwtRefreshTokenManagerImpl(jwtTokenProcessor, keyStorage);
  }

  // * MISCELLANEOUS
  @Bean
  Faker dataFaker() {
    return new Faker();
  }
}
