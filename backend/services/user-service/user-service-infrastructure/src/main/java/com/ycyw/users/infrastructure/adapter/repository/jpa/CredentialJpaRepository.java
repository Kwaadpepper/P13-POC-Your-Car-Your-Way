package com.ycyw.users.infrastructure.adapter.repository.jpa;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ycyw.users.infrastructure.entity.CredentialEntity;

public interface CredentialJpaRepository extends JpaRepository<CredentialEntity, UUID> {
  Optional<CredentialEntity> findByHashedIdentifier(String hashedIdentifier);

  Optional<CredentialEntity> findBySsoId(String ssoId);
}
