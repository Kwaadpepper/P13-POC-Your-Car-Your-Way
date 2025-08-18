package com.ycyw.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** Main entry point for the Gateway Spring Boot application. */
@SpringBootApplication
public class GatewayApplication {
  /**
   * Starts the Gateway Spring Boot application.
   *
   * @param args command-line arguments
   */
  public static void main(String[] args) {
    SpringApplication.run(GatewayApplication.class, args);
  }
}
