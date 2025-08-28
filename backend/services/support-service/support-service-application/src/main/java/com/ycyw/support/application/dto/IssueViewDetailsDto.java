package com.ycyw.support.application.dto;

import java.time.ZonedDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.jdt.annotation.Nullable;

public record IssueViewDetailsDto(
    UUID id,
    String subject,
    String description,
    @Nullable String answer,
    String status,
    ClientDto client,
    @Nullable ReservationDto reservation,
    @Nullable UUID conversation,
    ZonedDateTime updatedAt) {
  public record ClientDto(
      UUID id,
      @JsonProperty("first_name") String firstName,
      @JsonProperty("last_name") String lastName,
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

  public record ReservationDto(
      UUID id,
      String status,
      StartingFromDto from,
      ArrivingToDto to,
      VehiculeDto vehicle,
      String payment) {
    public record StartingFromDto(AgencyDto agency, ZonedDateTime at) {}

    public record ArrivingToDto(AgencyDto agency, ZonedDateTime at) {}

    public record VehiculeDto(String category) {}

    public record AgencyDto(
        String label, String phone, String email, AddressDto address, CoordinatesDto coordinates) {}

    public record CoordinatesDto(double lat, @JsonProperty("long") double lng) {}
  }
}
