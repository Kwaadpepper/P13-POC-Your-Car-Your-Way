package com.ycyw.users.domain.model.valueobject;

import com.ycyw.shared.utils.Domain;

public record SsoInfo(String providerId, SsoProvider provider) {
  public SsoInfo {
    Domain.checkDomain(() -> !providerId.isBlank(), "Provider ID cannot be blank");
  }
}
