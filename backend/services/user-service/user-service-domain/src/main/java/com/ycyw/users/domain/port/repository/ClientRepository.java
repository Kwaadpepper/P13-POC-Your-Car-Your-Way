package com.ycyw.users.domain.port.repository;

import java.util.UUID;

import com.ycyw.shared.ddd.lib.Repository;
import com.ycyw.users.domain.model.entity.client.Client;
import com.ycyw.users.domain.model.valueobject.Email;

import org.eclipse.jdt.annotation.Nullable;

public interface ClientRepository extends Repository<Client> {
  public @Nullable Client findByEmail(Email email);

  public @Nullable Client findByCredentialId(UUID credentialId);
}
