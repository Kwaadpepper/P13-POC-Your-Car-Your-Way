package com.ycyw.users.application.di;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ycyw.users.domain.port.repository.UserRepository;
import com.ycyw.users.domain.usecase.user.CreateUser;
import com.ycyw.users.domain.usecase.user.FindUser;

import net.datafaker.Faker;

@Configuration
public class SpringInjector {
  // * USECASES
  @Bean
  FindUser.FindUserHandler userService(UserRepository userRepository) {
    return new FindUser.FindUserHandler(userRepository);
  }

  @Bean
  CreateUser.CreateUserHandler createUserHandler(UserRepository userRepository) {
    return new CreateUser.CreateUserHandler(userRepository);
  }

  // * MISCELLANEOUS
  @Bean
  Faker dataFaker() {
    return new Faker();
  }
}
