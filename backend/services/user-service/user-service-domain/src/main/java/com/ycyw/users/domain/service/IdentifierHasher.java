package com.ycyw.users.domain.service;

import com.ycyw.users.domain.model.valueobject.HashedIdentifier;
import com.ycyw.users.domain.model.valueobject.RawIdentifier;
import com.ycyw.users.domain.port.service.Hasher;

public class IdentifierHasher {
  private final Hasher hasher;
  private static final Hasher.HashAlgorithm HASH_ALGORITHM = Hasher.HashAlgorithm.SHA256;

  public IdentifierHasher(Hasher hasher) {
    this.hasher = hasher;
  }

  public HashedIdentifier hash(RawIdentifier identifier) {
    return new HashedIdentifier(hasher.hash(identifier.value(), HASH_ALGORITHM));
  }
}
