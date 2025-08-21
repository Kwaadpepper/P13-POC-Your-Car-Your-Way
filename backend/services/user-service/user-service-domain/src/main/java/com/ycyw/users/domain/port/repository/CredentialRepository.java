package com.ycyw.users.domain.port.repository;

import com.ycyw.shared.ddd.lib.Repository;
import com.ycyw.users.domain.model.entity.credential.Credential;
import com.ycyw.users.domain.model.valueobject.HashedIdentifier;

import org.eclipse.jdt.annotation.Nullable;

public interface CredentialRepository extends Repository<Credential> {
  @Nullable Credential findByIdentifier(HashedIdentifier identifier);
}
