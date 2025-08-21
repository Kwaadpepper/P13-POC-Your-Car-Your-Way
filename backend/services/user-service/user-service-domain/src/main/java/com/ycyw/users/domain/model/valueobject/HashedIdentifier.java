package com.ycyw.users.domain.model.valueobject;

import com.ycyw.shared.utils.Domain;

public record HashedIdentifier(String value) {
  public HashedIdentifier {
    Domain.checkDomain(() -> !value.isBlank(), "Hash identifier cannot be blank");
  }

  @Override
  public String toString() {
    return "[PROTECTED]";
  }
}
