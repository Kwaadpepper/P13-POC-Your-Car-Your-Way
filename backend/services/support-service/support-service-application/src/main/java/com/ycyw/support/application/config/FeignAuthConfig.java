package com.ycyw.support.application.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import feign.RequestInterceptor;

@Configuration
public class FeignAuthConfig {

  @Bean
  public RequestInterceptor authForwardingInterceptor() {
    return template -> {
      final var attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
      if (attrs == null) {
        return;
      }
      final var request = attrs.getRequest();
      final var cookies = request.getCookies();
      if (cookies != null) {
        for (var cookie : cookies) {
          template.header("Cookie", cookie.getName() + "=" + cookie.getValue());
        }
      }
      final var auth = request.getHeader("Authorization");
      if (auth != null && !auth.isBlank()) {
        template.header("Authorization", auth);
      }
    };
  }
}
