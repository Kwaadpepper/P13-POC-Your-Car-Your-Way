package com.ycyw.configserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * Main entry point for the Spring Cloud Config Server application.
 *
 * <p>This class bootstraps the configuration server using Spring Boot and enables the Spring Cloud
 * Config Server functionality.
 */
@SpringBootApplication
@EnableConfigServer
public class ConfigServerApplication {
  /**
   * The entry point of the Config Server Spring Boot application.
   *
   * <p>This method starts the Spring application context using the specified arguments.
   *
   * @param args command-line arguments passed to the application
   */
  public static void main(String[] args) {
    SpringApplication.run(ConfigServerApplication.class, args);
  }
}
