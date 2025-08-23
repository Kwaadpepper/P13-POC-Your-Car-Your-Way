package com.ycyw.support;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/** Main entry point for the User Service Spring Boot application. */
@SpringBootApplication
@EnableFeignClients(basePackages = "com.ycyw.support.infrastructure.adapter.client")
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
