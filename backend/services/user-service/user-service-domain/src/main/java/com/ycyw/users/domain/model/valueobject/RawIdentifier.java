package com.ycyw.users.domain.model.valueobject;

import com.ycyw.shared.utils.Domain;

public record RawIdentifier(String value) {
  public RawIdentifier {
    Domain.checkDomain(() -> !value.isBlank(), "Raw identifier cannot be blank");
  }

  @Override
  public String toString() {
    return "[PROTECTED]";
  }
}
