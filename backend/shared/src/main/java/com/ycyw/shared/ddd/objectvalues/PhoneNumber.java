package com.ycyw.shared.ddd.objectvalues;

import java.util.regex.Pattern;

import com.ycyw.shared.utils.Domain;

public record PhoneNumber(String value) {
  private static final Pattern PATTERN = Pattern.compile("^\\+?[0-9 .\\-()]{6,20}$");

  public PhoneNumber {
    Domain.checkDomain(() -> !value.isBlank(), "Phone number cannot be blank");
    Domain.checkDomain(() -> PATTERN.matcher(value).matches(), "Invalid phone number format");
  }
}
