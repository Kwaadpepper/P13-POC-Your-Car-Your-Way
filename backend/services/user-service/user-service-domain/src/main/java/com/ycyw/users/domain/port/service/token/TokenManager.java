package com.ycyw.users.domain.port.service.token;

import org.eclipse.jdt.annotation.Nullable;

public interface TokenManager<T, C> {
  T generate(C claims);

  TokenValidity validate(T token);

  @Nullable C extract(T token);

  void invalidate(T token);

  sealed interface TokenValidity {
    final class Valid implements TokenValidity {}

    record Invalid(TokenInvalidityReason reason) implements TokenValidity {}
  }

  enum TokenInvalidityReason {
    REVOKED,
    EXPIRED,
    UNKNOWN,
  }
}
