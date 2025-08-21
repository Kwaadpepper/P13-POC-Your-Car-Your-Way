package com.ycyw.users.domain.model.entity.operator;

import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import com.ycyw.shared.ddd.lib.AggregateRoot;
import com.ycyw.shared.utils.Domain;
import com.ycyw.users.domain.model.entity.credential.CredentialId;
import com.ycyw.users.domain.model.valueobject.Email;
import com.ycyw.users.domain.model.valueobject.Role;

import org.eclipse.jdt.annotation.Nullable;

/**
 * Aggregate root Operator.
 *
 * <p>Correction principale : - Ajout d'un constructeur protégé qui prend un UUID pour permettre la
 * réconstitution (il appelle super(id) et donc utilise bien AggregateRoot(UUID)). - hydrate(...)
 * utilise maintenant ce constructeur protégé au lieu de créer une instance "base" puis d'envelopper
 * dans une sous-classe (technique qui empêchait l'appel du constructeur AggregateRoot(UUID)).
 *
 * <p>Autres améliorations : - validations null/blank via Domain.checkDomain et
 * Objects.requireNonNull - equals/hashCode corrigés et simplifiés
 */
public class Operator extends AggregateRoot {
  private final String name;
  private final Email email;
  private final Set<Role> roles;
  private final CredentialId credentialId;
  private final ZonedDateTime updatedAt;
  private final @Nullable ZonedDateTime deletedAt;

  private static void validateCommonInvariants(
      String name,
      Email email,
      Set<Role> roles,
      CredentialId credentialId,
      ZonedDateTime updatedAt) {
    Objects.requireNonNull(name, "name");
    Objects.requireNonNull(email, "email");
    Objects.requireNonNull(roles, "roles");
    Objects.requireNonNull(credentialId, "credentialId");
    Objects.requireNonNull(updatedAt, "updatedAt");

    Domain.checkDomain(() -> !name.isBlank(), "Name cannot be blank");
    Domain.checkDomain(() -> !roles.isEmpty(), "Operator must have at least one role");
  }

  /** Constructor used for creating new aggregates (generates a new id via super()). */
  public Operator(
      String name,
      Email email,
      Set<Role> roles,
      CredentialId credentialId,
      ZonedDateTime updatedAt,
      @Nullable ZonedDateTime deletedAt) {
    super();
    validateCommonInvariants(name, email, roles, credentialId, updatedAt);
    this.name = name;
    this.email = email;
    this.roles = Set.copyOf(roles);
    this.credentialId = credentialId;
    this.updatedAt = updatedAt;
    this.deletedAt = deletedAt;
  }

  protected Operator(
      UUID id,
      String name,
      Email email,
      Set<Role> roles,
      CredentialId credentialId,
      ZonedDateTime updatedAt,
      @Nullable ZonedDateTime deletedAt) {
    super(id);
    validateCommonInvariants(name, email, roles, credentialId, updatedAt);
    this.name = name;
    this.email = email;
    this.roles = Set.copyOf(roles);
    this.credentialId = credentialId;
    this.updatedAt = updatedAt;
    this.deletedAt = deletedAt;
  }

  public static Operator hydrate(
      UUID id,
      String name,
      Email email,
      Set<Role> roles,
      CredentialId credentialId,
      ZonedDateTime updatedAt,
      @Nullable ZonedDateTime deletedAt) {
    return new Operator(id, name, email, roles, credentialId, updatedAt, deletedAt);
  }

  @Override
  public UUID getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Email getEmail() {
    return email;
  }

  public Set<Role> getRoles() {
    return roles;
  }

  public CredentialId getCredentialId() {
    return credentialId;
  }

  @Override
  public ZonedDateTime getCreatedAt() {
    return createdAt;
  }

  public @Nullable ZonedDateTime getUpdatedAt() {
    return updatedAt;
  }

  public @Nullable ZonedDateTime getDeletedAt() {
    return deletedAt;
  }

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof Operator that)) {
      return false;
    }
    return getId().equals(that.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
