package com.ycyw.users.infrastructure.adapter.repository.jpa;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ycyw.users.infrastructure.entity.ClientEntity;

public interface ClientJpaRepository extends JpaRepository<ClientEntity, UUID> {
  Optional<ClientEntity> findByEmailIgnoreCase(String email);

  Optional<ClientEntity> findByCredentialId(UUID credentialUuid);
}
