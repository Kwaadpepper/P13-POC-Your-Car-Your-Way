package com.ycyw.users.infrastructure.adapter.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ycyw.users.domain.model.valueobject.HashedPassword;
import com.ycyw.users.domain.model.valueobject.PasswordCandidate;
import com.ycyw.users.domain.model.valueobject.RawPassword;
import com.ycyw.users.domain.port.service.PasswordHasher;

@Service
public class PasswordHasherImpl implements PasswordHasher {
  private final PasswordEncoder passwordEncoder;

  public PasswordHasherImpl(final PasswordEncoder passwordEncoder) {
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public HashedPassword hash(RawPassword rawPassword) {
    final var passwordValue = rawPassword.value();

    return new HashedPassword(passwordEncoder.encode(passwordValue));
  }

  @Override
  public boolean verify(PasswordCandidate password, HashedPassword hashedPassword) {
    final var passwordValue = password.value();
    final var hashedPasswordValue = hashedPassword.value();

    return passwordEncoder.matches(passwordValue, hashedPasswordValue);
  }
}
