package com.ycyw.serviceregistry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Main entry point for the Service Registry application.
 *
 * <p>This class bootstraps the Spring Boot application and enables the Eureka Server.
 */
@SpringBootApplication
@EnableEurekaServer
public class ServiceRegistryApplication {
  /**
   * Starts the Service Registry application.
   *
   * @param args command-line arguments
   */
  public static void main(String[] args) {
    SpringApplication.run(ServiceRegistryApplication.class, args);
  }
}
