package com.ycyw.users.application.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.ycyw.users.application.seeder.UserAccountSeeder;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
public class SpringBootConfig {
  private static final Logger logger = LoggerFactory.getLogger(SpringBootConfig.class);
  private final Environment environment;
  private final UserAccountSeeder userAccountSeeder;

  SpringBootConfig(Environment environment, UserAccountSeeder userAccountSeeder) {
    this.environment = environment;
    this.userAccountSeeder = userAccountSeeder;
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
    // This method can be used to seed initial data if needed
    // For example, you can call a seeder class here
    logger.info("Seeding initial data...");

    userAccountSeeder.seed();

    logger.info("Seeding completed.");
  }
}
