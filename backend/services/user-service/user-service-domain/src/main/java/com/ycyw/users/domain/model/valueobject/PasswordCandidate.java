package com.ycyw.users.domain.model.valueobject;

import com.ycyw.shared.utils.Domain;

public record PasswordCandidate(String value) {
  public PasswordCandidate {
    Domain.checkDomain(() -> !value.isBlank(), "password cannot be blank");
  }

  @Override
  public String toString() {
    return "[PROTECTED]";
  }
}
