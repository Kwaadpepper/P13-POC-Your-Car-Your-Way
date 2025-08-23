package com.ycyw.support.infrastructure.feignclient;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.eclipse.jdt.annotation.Nullable;

@FeignClient(
    name = "user-service-application",
    path = "/clients",
    contextId = "ClientServiceFeignClient")
public interface ClientServiceFeignClient {

  @GetMapping("{user}")
  ClientDto getClientByUuidOrEmail(@PathVariable("user") String user);

  @JsonIgnoreProperties(ignoreUnknown = true)
  public record ClientDto(
      UUID id,
      String lastName,
      String firstName,
      String email,
      String phone,
      LocalDate birthDate,
      AddressDto address,
      ZonedDateTime updatedAt,
      @Nullable ZonedDateTime deletedAt) {}

  @JsonIgnoreProperties(ignoreUnknown = true)
  public record AddressDto(
      String line1,
      @Nullable String line2,
      @Nullable String line3,
      String city,
      String zipCode,
      String country) {}
}
