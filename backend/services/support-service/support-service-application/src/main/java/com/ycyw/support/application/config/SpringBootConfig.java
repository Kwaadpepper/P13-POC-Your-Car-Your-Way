package com.ycyw.support.application.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.ycyw.support.application.seeder.FaqSeeder;
import com.ycyw.support.application.seeder.IssueSeeder;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
public class SpringBootConfig {
  private static final Logger logger = LoggerFactory.getLogger(SpringBootConfig.class);
  private final Environment environment;
  private final FaqSeeder faqSeeder;
  private final IssueSeeder issueSeeder;

  SpringBootConfig(Environment environment, FaqSeeder faqSeeder, IssueSeeder issueSeeder) {
    this.environment = environment;
    this.faqSeeder = faqSeeder;
    this.issueSeeder = issueSeeder;
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
    faqSeeder.seed();
    issueSeeder.seed();
    logger.info("Seeding completed.");
  }
}
