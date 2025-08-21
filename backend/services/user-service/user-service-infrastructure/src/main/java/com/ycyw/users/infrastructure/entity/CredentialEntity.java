package com.ycyw.users.infrastructure.entity;

import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.eclipse.jdt.annotation.Nullable;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(schema = "user_context", name = "credentials")
public class CredentialEntity {

  @Id
  @Column(columnDefinition = "uuid")
  private UUID id;

  @Column(name = "last_connection", nullable = true)
  @Nullable private ZonedDateTime lastConnection;

  @Column(name = "hashed_identifier", length = 250, nullable = false)
  private String hashedIdentifier;

  @Column(name = "hashed_password", length = 350, nullable = false)
  private String hashedPassword;

  @Column(name = "sso_id", length = 450, nullable = true)
  @Nullable private String ssoId;

  @JdbcTypeCode(SqlTypes.SMALLINT)
  @Column(name = "sso_provider", nullable = true)
  @Nullable private Integer ssoProvider;

  @Column(name = "topt_code_value", length = 350, nullable = true)
  @Nullable private String toptCodeValue;

  @Column(name = "passkey_id", columnDefinition = "uuid", unique = true, nullable = true)
  @Nullable private UUID passkeyId;

  @Column(name = "passkey_publickey", columnDefinition = "bytea", nullable = true)
  @Nullable private Byte[] passkeyPublicKey;

  @Column(name = "passkey_sign_count", nullable = true)
  @Nullable private Integer passkeySignCount;

  @Column(name = "passkey_type", length = 255, nullable = true)
  @Nullable private String passkeyType;

  public CredentialEntity() {
    // JPA requires a no-arg constructor
  }

  // Getters / Setters
  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public @Nullable ZonedDateTime getLastConnection() {
    return lastConnection;
  }

  public void setLastConnection(@Nullable ZonedDateTime lastConnection) {
    this.lastConnection = lastConnection;
  }

  public String getHashedIdentifier() {
    return hashedIdentifier;
  }

  public void setHashedIdentifier(String hashedIdentifier) {
    this.hashedIdentifier = hashedIdentifier;
  }

  public String getHashedPassword() {
    return hashedPassword;
  }

  public void setHashedPassword(String hashedPassword) {
    this.hashedPassword = hashedPassword;
  }

  public @Nullable String getSsoId() {
    return ssoId;
  }

  public void setSsoId(@Nullable String ssoId) {
    this.ssoId = ssoId;
  }

  public @Nullable Integer getSsoProvider() {
    return ssoProvider;
  }

  public void setSsoProvider(@Nullable Integer ssoProvider) {
    this.ssoProvider = ssoProvider;
  }

  public @Nullable String getToptCodeValue() {
    return toptCodeValue;
  }

  public void setToptCodeValue(@Nullable String toptCodeValue) {
    this.toptCodeValue = toptCodeValue;
  }

  public @Nullable UUID getPasskeyId() {
    return passkeyId;
  }

  public void setPasskeyId(@Nullable UUID passkeyId) {
    this.passkeyId = passkeyId;
  }

  public @Nullable Byte[] getPasskeyPublicKey() {
    return passkeyPublicKey;
  }

  public void setPasskeyPublicKey(@Nullable Byte[] passkeyPublicKey) {
    this.passkeyPublicKey = passkeyPublicKey;
  }

  public @Nullable Integer getPasskeySignCount() {
    return passkeySignCount;
  }

  public void setPasskeySignCount(@Nullable Integer passkeySignCount) {
    this.passkeySignCount = passkeySignCount;
  }

  public @Nullable String getPasskeyType() {
    return passkeyType;
  }

  public void setPasskeyType(@Nullable String passkeyType) {
    this.passkeyType = passkeyType;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof CredentialEntity)) {
      return false;
    }
    CredentialEntity that = (CredentialEntity) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
