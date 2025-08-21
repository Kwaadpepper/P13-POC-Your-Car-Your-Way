package com.ycyw.users.domain.model.valueobject;

import java.util.List;
import java.util.UUID;

import com.ycyw.shared.utils.Domain;

public record PartialPassKeysInfo(UUID id, List<Byte> publicKey, Integer signCount, String type) {
  public PartialPassKeysInfo {
    Domain.checkDomain(() -> !publicKey.isEmpty(), "Public key cannot be blank");
    Domain.checkDomain(() -> signCount >= 0, "Sign count must be non-negative");
    Domain.checkDomain(() -> type != null && !type.isBlank(), "Type cannot be blank");
  }
}
