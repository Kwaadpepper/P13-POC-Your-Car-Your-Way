package com.ycyw.users.domain.model.valueobject;

import com.ycyw.shared.utils.Domain;

public record ToptCode(String value) {
  public ToptCode {
    Domain.checkDomain(() -> !value.isBlank(), "ToptCode cannot be blank");
    Domain.checkDomain(() -> value.length() == 6, "ToptCode must be exactly 6 characters long");
  }

  @Override
  public String toString() {
    return "[PROTECTED]";
  }
}
