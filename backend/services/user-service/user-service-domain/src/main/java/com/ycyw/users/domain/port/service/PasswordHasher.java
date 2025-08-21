package com.ycyw.users.domain.port.service;

import com.ycyw.users.domain.model.valueobject.HashedPassword;
import com.ycyw.users.domain.model.valueobject.PasswordCandidate;
import com.ycyw.users.domain.model.valueobject.RawPassword;

public interface PasswordHasher {
  HashedPassword hash(RawPassword password);

  boolean verify(PasswordCandidate passwordCandidate, HashedPassword hashedPassword);
}
