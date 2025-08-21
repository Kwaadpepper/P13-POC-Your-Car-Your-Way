package com.ycyw.users.application.di;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ycyw.users.domain.port.repository.ClientRepository;
import com.ycyw.users.domain.port.repository.CredentialRepository;
import com.ycyw.users.domain.port.service.Hasher;
import com.ycyw.users.domain.port.service.PasswordHasher;
import com.ycyw.users.domain.service.IdentifierHasher;
import com.ycyw.users.domain.usecase.client.CreateClient;
import com.ycyw.users.domain.usecase.client.FindClient;

import net.datafaker.Faker;

@Configuration
public class SpringInjector {
  // * USECASES
  @Bean
  FindClient.FindUserHandler userService(ClientRepository clientRepository) {
    return new FindClient.FindUserHandler(clientRepository);
  }

  @Bean
  CreateClient.CreateClientHandler createUserHandler(
      CredentialRepository credentialRepository,
      ClientRepository clientRepository,
      IdentifierHasher hasher,
      PasswordHasher passwordHasher) {
    return new CreateClient.CreateClientHandler(
        credentialRepository, clientRepository, hasher, passwordHasher);
  }

  // * OTHER DOMAIN SERVICES
  @Bean
  IdentifierHasher identifierHasher(Hasher hasher) {
    return new IdentifierHasher(hasher);
  }

  // * MISCELLANEOUS
  @Bean
  Faker dataFaker() {
    return new Faker();
  }
}
