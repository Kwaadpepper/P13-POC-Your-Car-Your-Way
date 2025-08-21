package com.ycyw.users.domain.port.repository;

import java.util.UUID;

import com.ycyw.shared.ddd.lib.Repository;
import com.ycyw.shared.ddd.objectvalues.Email;
import com.ycyw.users.domain.model.entity.operator.Operator;

import org.eclipse.jdt.annotation.Nullable;

public interface OperatorRepository extends Repository<Operator> {
  public @Nullable Operator findByEmail(Email email);

  public @Nullable Operator findByCredentialId(UUID credentialId);
}
