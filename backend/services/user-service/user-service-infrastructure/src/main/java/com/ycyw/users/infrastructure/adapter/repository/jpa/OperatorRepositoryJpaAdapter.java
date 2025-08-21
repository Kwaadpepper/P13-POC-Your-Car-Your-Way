package com.ycyw.users.infrastructure.adapter.repository.jpa;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.ycyw.users.domain.model.entity.credential.CredentialId;
import com.ycyw.users.domain.model.entity.operator.Operator;
import com.ycyw.users.domain.model.valueobject.Email;
import com.ycyw.users.domain.model.valueobject.Role;
import com.ycyw.users.domain.port.repository.OperatorRepository;
import com.ycyw.users.infrastructure.entity.OperatorEntity;

import org.eclipse.jdt.annotation.Nullable;

@Repository
public class OperatorRepositoryJpaAdapter implements OperatorRepository {

  private final OperatorJpaRepository repo;

  public OperatorRepositoryJpaAdapter(OperatorJpaRepository repo) {
    this.repo = Objects.requireNonNull(repo, "repo");
  }

  @Override
  public @Nullable Operator find(UUID id) {
    var e = repo.findById(id);
    if (e.isEmpty()) {
      return null;
    }
    return repo.findById(id).map(this::toDomain).get();
  }

  @Override
  public @Nullable Operator findByCredentialId(UUID credentialId) {
    var e = repo.findByCredentialId(credentialId);
    if (e.isEmpty()) {
      return null;
    }
    return e.map(this::toDomain).get();
  }

  @Override
  public void save(Operator entity) {
    repo.save(toEntity(entity));
  }

  @Override
  public void update(Operator entity) {
    repo.save(toEntity(entity));
  }

  @Override
  public void delete(Operator entity) {
    repo.deleteById(entity.getId());
  }

  @Override
  public @Nullable Operator findByEmail(Email email) {
    var e = repo.findByEmailIgnoreCase(email.value());
    if (e.isEmpty()) {
      return null;
    }
    return e.map(this::toDomain).get();
  }

  // --- Mapping helpers ---

  private Operator toDomain(OperatorEntity e) {
    var email = new Email(e.getEmail());
    var roles =
        e.getRoles().stream()
            .map(String::toUpperCase)
            .map(this::toRole)
            .collect(Collectors.toUnmodifiableSet());

    var credentialId = new CredentialId(e.getCredentialId());

    return Operator.hydrate(
        e.getId(), e.getName(), email, roles, credentialId, e.getUpdatedAt(), e.getDeletedAt());
  }

  private OperatorEntity toEntity(Operator domain) {
    Set<String> mappedRoles =
        domain.getRoles().stream()
            .map(this::fromRole)
            .map(String::toUpperCase)
            .collect(Collectors.toSet());
    var e = new OperatorEntity();
    e.setId(domain.getId());
    e.setName(domain.getName());
    e.setEmail(domain.getEmail().value());
    e.setRoles(mappedRoles);
    e.setCredentialId(domain.getCredentialId().value());
    e.setUpdatedAt(domain.getUpdatedAt());
    e.setDeletedAt(domain.getDeletedAt());
    return e;
  }

  private Role toRole(String s) {
    return switch (s) {
      case "ADMIN" -> Role.ADMIN;
      case "COMMERCIAL" -> Role.COMMERCIAL;
      case "SUPPORT" -> Role.SUPPORT;
      default -> throw new IllegalArgumentException("Unknown role: " + s);
    };
  }

  private String fromRole(Role role) {
    return switch (role) {
      case ADMIN -> "ADMIN";
      case COMMERCIAL -> "COMMERCIAL";
      case SUPPORT -> "SUPPORT";
    };
  }
}
