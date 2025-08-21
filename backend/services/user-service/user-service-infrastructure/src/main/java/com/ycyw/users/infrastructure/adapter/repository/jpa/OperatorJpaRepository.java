package com.ycyw.users.infrastructure.adapter.repository.jpa;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ycyw.users.infrastructure.entity.OperatorEntity;

public interface OperatorJpaRepository extends JpaRepository<OperatorEntity, UUID> {
  Optional<OperatorEntity> findByEmailIgnoreCase(String email);

  Optional<OperatorEntity> findByCredentialId(UUID credentialId);
}
