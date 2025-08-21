package com.ycyw.users.application.dto;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;

import org.eclipse.jdt.annotation.Nullable;

public record ClientViewDto(
    UUID id,
    String lastName,
    String firstName,
    String email,
    String phone,
    LocalDate birthDate,
    AddressViewDto address,
    ZonedDateTime updatedAt,
    @Nullable ZonedDateTime deletedAt) {
  public record AddressViewDto(
      String line1,
      @Nullable String line2,
      @Nullable String line3,
      String city,
      String zipCode,
      String country) {}
}
