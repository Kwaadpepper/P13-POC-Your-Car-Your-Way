package com.ycyw.users.application.di;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ycyw.users.domain.port.repository.UserRepository;
import com.ycyw.users.domain.usecase.user.GetUserHandler;

@Configuration
public class SpringInjector {
  // * USECASES
  @Bean
  GetUserHandler userService(UserRepository userRepository) {
    return new GetUserHandler(userRepository);
  }
}
