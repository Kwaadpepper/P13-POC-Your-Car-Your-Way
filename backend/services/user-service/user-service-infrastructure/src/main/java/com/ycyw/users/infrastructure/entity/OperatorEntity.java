package com.ycyw.users.infrastructure.entity;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.eclipse.jdt.annotation.Nullable;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(schema = "user_context", name = "operators")
public class OperatorEntity {

  @Id
  @Column(columnDefinition = "uuid")
  private UUID id;

  @Column(nullable = false, length = 255)
  private String name;

  @Column(nullable = false, length = 255, unique = true)
  private String email;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(nullable = false, columnDefinition = "jsonb")
  private Set<String> roles;

  // Référence d'agrégat : identifiant UUID du Credential (non null d'après DDL)
  @Column(name = "credential", columnDefinition = "uuid", nullable = false, unique = true)
  private UUID credentialId;

  @Column(name = "updated_at", nullable = false)
  private ZonedDateTime updatedAt;

  @Column(name = "deleted_at", nullable = true)
  private @Nullable ZonedDateTime deletedAt;

  public OperatorEntity() {
    // JPA requires a no-arg constructor
    this.id = UUID.randomUUID();
    this.name = "";
    this.email = "";
    this.roles = Set.of();
    this.credentialId = UUID.randomUUID();
    this.updatedAt = ZonedDateTime.now(ZoneId.systemDefault());
    this.deletedAt = null;
  }

  // Getters / Setters
  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Set<String> getRoles() {
    return roles;
  }

  public void setRoles(Set<String> roles) {
    this.roles = roles;
  }

  public UUID getCredentialId() {
    return credentialId;
  }

  public void setCredentialId(UUID credentialId) {
    this.credentialId = credentialId;
  }

  public ZonedDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(ZonedDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  public @Nullable ZonedDateTime getDeletedAt() {
    return deletedAt;
  }

  public void setDeletedAt(@Nullable ZonedDateTime deletedAt) {
    this.deletedAt = deletedAt;
  }

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof OperatorEntity that)) {
      return false;
    }
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
