package com.ycyw.support.application.dto;

import java.net.URI;
import java.time.DayOfWeek;
import java.util.Map;

import org.eclipse.jdt.annotation.Nullable;

public record CompanyInfoViewDto(
    AddressViewDto address, BusinessDto phone, BusinessDto chat, String email, URI website) {
  public static record AddressViewDto(
      String line1,
      @Nullable String line2,
      @Nullable String line3,
      String city,
      String zipCode,
      String country) {}

  public static record BusinessDto(
      @Nullable String phone, Map<DayOfWeek, TimeRangeDto> businessHours) {}

  public static record TimeRangeDto(String from, String to) {}
}
