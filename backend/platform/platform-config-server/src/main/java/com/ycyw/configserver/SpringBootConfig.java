package com.ycyw.configserver;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
public class SpringBootConfig {
  private static final Logger logger = LoggerFactory.getLogger(SpringBootConfig.class);
  private final Environment environment;

  SpringBootConfig(Environment environment) {
    this.environment = environment;
  }

  @PostConstruct
  public void init() {
    logger.info(
        "Starting {} with Loki URL={}",
        environment.getProperty("spring.application.name"),
        environment.getProperty("config.loki.url"));
  }
}
