package com.ycyw.users.application.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.ycyw.users.application.seeder.ClientAccountSeeder;
import com.ycyw.users.application.seeder.OperatorAccountSeeder;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
public class SpringBootConfig {
  private static final Logger logger = LoggerFactory.getLogger(SpringBootConfig.class);
  private final Environment environment;
  private final ClientAccountSeeder clientAccountSeeder;
  private final OperatorAccountSeeder operatorAccountSeeder;

  SpringBootConfig(
      Environment environment,
      ClientAccountSeeder clientAccountSeeder,
      OperatorAccountSeeder operatorAccountSeeder) {
    this.environment = environment;
    this.clientAccountSeeder = clientAccountSeeder;
    this.operatorAccountSeeder = operatorAccountSeeder;
  }

  @PostConstruct
  public void init() {
    if (hasToSeedData(environment)) {
      seed();
    }
  }

  private boolean hasToSeedData(Environment environment) {
    String hasToSeed = environment.getProperty("config.seed-data");
    return null != hasToSeed && !hasToSeed.equals("false");
  }

  private void seed() {
    logger.info("Seeding initial data...");

    clientAccountSeeder.seed();
    operatorAccountSeeder.seed();

    logger.info("Seeding completed.");
  }
}
