package com.ycyw.support.application.dto;

import java.time.ZonedDateTime;
import java.util.UUID;

import org.eclipse.jdt.annotation.Nullable;

public record IssueViewDetailsDto(
    UUID id,
    String subject,
    String description,
    String status,
    ClientDto client,
    @Nullable UUID reservation,
    ZonedDateTime updatedAt) {
  public record ClientDto(
      UUID id,
      String firstName,
      String lastName,
      String email,
      String phone,
      String birthdate,
      AddressDto address) {}

  public record AddressDto(
      String line1,
      @Nullable String line2,
      @Nullable String line3,
      String city,
      String zipCode,
      String country) {}
}
