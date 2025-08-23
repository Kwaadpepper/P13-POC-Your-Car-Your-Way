package com.ycyw.support.infrastructure.feignclient;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.ycyw.shared.ddd.objectvalues.JwtAccessToken;

@FeignClient(
    name = "user-service-application",
    path = "/auth",
    contextId = "SessionVerifyerFeignClient")
public interface SessionVerifyerFeignClient {

  @GetMapping("/verify")
  UserDetailsDto verify(JwtAccessToken jwtAccessToken);

  public record UserDetailsDto(UUID subject, String username, String role) {}
}
