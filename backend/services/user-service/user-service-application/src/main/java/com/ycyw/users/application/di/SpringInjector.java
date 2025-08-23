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
  private final KeyStorage keyStorage;
  private final Hasher hasher;
  private final IdentifierHasher identifierHasher;
  private final PasswordHasher passwordHasher;

  private final OperatorRepository operatorRepository;
  private final ClientRepository clientRepository;
  private final CredentialRepository credentialRepository;

  private final SessionService sessionService;

  private final JwtAccessTokenManager jwtAccessTokenManager;
  private final JwtRefreshTokenManager jwtRefreshTokenManager;

  public SpringInjector(
      AppConfiguration appConfiguration,
      KeyStorage keyStorage,
      Hasher hasher,
      IdentifierHasher identifierHasher,
      PasswordHasher passwordHasher,
      OperatorRepository operatorRepository,
      ClientRepository clientRepository,
      CredentialRepository credentialRepository,
      SessionService sessionService,
      JwtAccessTokenManager jwtAccessTokenManager,
      JwtRefreshTokenManager jwtRefreshTokenManager) {
    this.appConfiguration = appConfiguration;
    this.keyStorage = keyStorage;
    this.hasher = hasher;
    this.identifierHasher = identifierHasher;
    this.passwordHasher = passwordHasher;
    this.operatorRepository = operatorRepository;
    this.clientRepository = clientRepository;
    this.credentialRepository = credentialRepository;
    this.sessionService = sessionService;
    this.jwtAccessTokenManager = jwtAccessTokenManager;
    this.jwtRefreshTokenManager = jwtRefreshTokenManager;
  }

  // * USECASES
  @Bean
  FindClient.Handler createUser(ClientRepository clientRepository) {
    return new FindClient.Handler(clientRepository);
  }

  @Bean
  CreateClient.Handler createUserHandler() {
    return new CreateClient.Handler(
        credentialRepository, clientRepository, identifierHasher, passwordHasher);
  }

  @Bean
  FindOperator.Handler createOperator() {
    return new FindOperator.Handler(operatorRepository);
  }

  @Bean
  CreateOperator.Handler createOperatorHandler() {
    return new CreateOperator.Handler(
        credentialRepository, operatorRepository, identifierHasher, passwordHasher);
  }

  @Bean
  CreateSession.Handler createSessionHandler() {
    return new CreateSession.Handler(
        credentialRepository,
        operatorRepository,
        clientRepository,
        sessionService,
        identifierHasher,
        passwordHasher);
  }

  @Bean
  InvalidateSession.Handler invalidateSessionHandler() {
    return new InvalidateSession.Handler(credentialRepository, sessionService);
  }

  @Bean
  RefreshSession.Handler refreshSessionHandler() {
    return new RefreshSession.Handler(credentialRepository, sessionService);
  }

  @Bean
  VerifySession.Handler verifySessionHandler() {
    return new VerifySession.Handler(clientRepository, operatorRepository, sessionService);
  }

  // * OTHER DOMAIN SERVICES
  @Bean
  SessionService sessionService() {
    return new SessionService(jwtAccessTokenManager, jwtRefreshTokenManager);
  }

  @Bean
  IdentifierHasher identifierHasher() {
    return new IdentifierHasher(hasher);
  }

  @Bean
  JwtAccessTokenManager jwtAccessTokenManager() {
    final var appName = appConfiguration.getAppName();
    final var jwtSecretKey = appConfiguration.getJwtSecretKey();
    final var jwtTokenExpiration = appConfiguration.getJwtTokenExpiration();
    final var jwtTokenProcessor =
        new JwtTokenProcessorImpl(jwtTokenExpiration, jwtSecretKey, appName);

    return new JwtAccessTokenManagerImpl(jwtTokenProcessor, keyStorage);
  }

  @Bean
  JwtRefreshTokenManager jwtRefreshTokenManager() {
    final var appName = appConfiguration.getAppName();
    final var jwtSecretKey = appConfiguration.getJwtSecretKey();
    final var jwtRefreshExpiration = appConfiguration.getJwtRefreshExpiration();
    final var jwtTokenProcessor =
        new JwtTokenProcessorImpl(jwtRefreshExpiration, jwtSecretKey, appName);

    return new JwtRefreshTokenManagerImpl(jwtTokenProcessor, keyStorage);
  }

  // * MISCELLANEOUS
  @Bean
  Faker dataFaker() {
    return new Faker();
  }
}
