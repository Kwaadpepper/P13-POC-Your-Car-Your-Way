package com.ycyw.support.application.di;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import net.datafaker.Faker;

@Configuration
public class SpringInjector {
  // * USECASES

  // * OTHER DOMAIN SERVICES

  // * MISCELLANEOUS
  @Bean
  Faker dataFaker() {
    return new Faker();
  }
}
