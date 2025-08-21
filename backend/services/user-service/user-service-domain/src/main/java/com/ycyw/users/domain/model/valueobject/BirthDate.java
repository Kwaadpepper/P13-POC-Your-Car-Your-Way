package com.ycyw.users.domain.model.valueobject;

import java.time.LocalDate;
import java.time.ZoneId;

import com.ycyw.shared.utils.Domain;

public record BirthDate(LocalDate value) {
  public BirthDate {
    Domain.checkDomain(
        () -> !value.isAfter(LocalDate.now(ZoneId.systemDefault())),
        "Birth date cannot be in the future");
  }
}
