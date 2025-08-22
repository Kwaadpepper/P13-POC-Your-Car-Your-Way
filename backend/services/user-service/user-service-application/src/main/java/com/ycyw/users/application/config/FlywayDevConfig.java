package com.ycyw.users.application.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import jakarta.annotation.PostConstruct;
import org.flywaydb.core.Flyway;

/**
 * This is for developpment purpose only. It will clean the database on each start. This is in order
 * to make sure seeding is ok. It wont hurt if you launch multiple instances of the the service
 */
@Configuration
@Profile("dev")
public class FlywayDevConfig {
  private final Flyway flyway;

  FlywayDevConfig(Flyway flyway) {
    this.flyway = flyway;
  }

  @PostConstruct
  public void cleanAndMigrate() {
    flyway.clean();
  }
}
