package com.ycyw.users.domain.model.valueobject;

import java.time.LocalDate;

import com.ycyw.shared.utils.Domain;

public record BirthDate(LocalDate value) {
  public BirthDate {
    Domain.checkDomain(() -> !value.isAfter(LocalDate.now()), "Birth date cannot be in the future");
  }
}
