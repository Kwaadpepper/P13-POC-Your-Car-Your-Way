package com.ycyw.gateway.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import org.eclipse.jdt.annotation.Nullable;

@Configuration
@ConfigurationProperties(prefix = "security")
public class PublicRoutesConfig {
  private List<String> publicRoutes = List.of();

  @Nullable public List<String> getPublicRoutes() {
    return publicRoutes;
  }

  public void setPublicRoutes(List<String> publicRoutes) {
    this.publicRoutes = publicRoutes;
  }
}
