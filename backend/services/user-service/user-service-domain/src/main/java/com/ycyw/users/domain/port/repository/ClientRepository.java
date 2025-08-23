package com.ycyw.users.domain.port.repository;

import java.util.UUID;

import com.ycyw.shared.ddd.lib.Repository;
import com.ycyw.shared.ddd.objectvalues.Email;
import com.ycyw.users.domain.model.entity.client.Client;

import org.eclipse.jdt.annotation.Nullable;

public interface ClientRepository extends Repository<Client> {
  public @Nullable Client findByEmail(Email email);

  public @Nullable Client findByCredentialId(UUID credentialId);
}
