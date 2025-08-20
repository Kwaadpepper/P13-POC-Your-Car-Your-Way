package com.ycyw.users.application.di;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ycyw.users.domain.port.repository.UserRepository;
import com.ycyw.users.domain.usecase.user.CreateUser;
import com.ycyw.users.domain.usecase.user.GetUser;

import net.datafaker.Faker;

@Configuration
public class SpringInjector {
  // * USECASES
  @Bean
  GetUser.GetUserHandler userService(UserRepository userRepository) {
    return new GetUser.GetUserHandler(userRepository);
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
