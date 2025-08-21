package com.ycyw.users.infrastructure.entity;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
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
  private byte[] passkeyPublicKey;

  @Column(name = "passkey_sign_count", nullable = true)
  @Nullable private Integer passkeySignCount;

  @Column(name = "passkey_type", length = 255, nullable = true)
  @Nullable private String passkeyType;

  public CredentialEntity() {
    // JPA requires a no-arg constructor
    this.id = UUID.randomUUID();
    this.lastConnection = null;
    this.hashedIdentifier = "";
    this.hashedPassword = "";
    this.ssoId = null;
    this.ssoProvider = null;
    this.toptCodeValue = null;
    this.passkeyId = null;
    this.passkeyPublicKey = new byte[0];
    this.passkeySignCount = null;
    this.passkeyType = null;
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

  @SuppressWarnings("unused")
  public @Nullable List<Byte> getPasskeyPublicKey() {
    if (passkeyPublicKey == null) {
      return null;
    }
    List<Byte> list = new ArrayList<>(passkeyPublicKey.length);
    for (byte b : passkeyPublicKey) {
      list.add(b);
    }
    return list;
  }

  public void setPasskeyPublicKey(@Nullable List<Byte> passkeyPublicKey) {
    if (passkeyPublicKey == null) {
      this.passkeyPublicKey = new byte[0];
    } else {
      this.passkeyPublicKey = new byte[passkeyPublicKey.size()];
      for (int i = 0; i < passkeyPublicKey.size(); i++) {
        @Nullable Byte b = passkeyPublicKey.get(i);
        this.passkeyPublicKey[i] = (b == null) ? 0 : b;
      }
    }
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
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof CredentialEntity that)) {
      return false;
    }
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
