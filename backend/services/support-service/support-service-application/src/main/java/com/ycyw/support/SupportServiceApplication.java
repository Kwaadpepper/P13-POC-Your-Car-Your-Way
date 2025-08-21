package com.ycyw.support;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** Main entry point for the User Service Spring Boot application. */
@SpringBootApplication
public class SupportServiceApplication {
  /**
   * Starts the User Service Spring Boot application.
   *
   * @param args command-line arguments passed to the application
   */
  public static void main(String[] args) {
    SpringApplication.run(SupportServiceApplication.class, args);
  }
}
