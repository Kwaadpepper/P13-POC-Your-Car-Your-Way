package com.ycyw.users.domain.model.entity.credential;

import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

import com.ycyw.shared.ddd.lib.AggregateRoot;
import com.ycyw.users.domain.model.valueobject.HashedIdentifier;
import com.ycyw.users.domain.model.valueobject.HashedPassword;
import com.ycyw.users.domain.model.valueobject.PartialPassKeysInfo;
import com.ycyw.users.domain.model.valueobject.SsoInfo;
import com.ycyw.users.domain.model.valueobject.ToptCode;

import org.eclipse.jdt.annotation.Nullable;

public class Credential extends AggregateRoot {

  private final HashedPassword hashedPassword;
  private final HashedIdentifier hashedIdentifier;
  private final @Nullable SsoInfo ssoInfo;
  private final @Nullable ToptCode toptCode;
  private final @Nullable PartialPassKeysInfo partialPassKeysInfo;
  private final @Nullable ZonedDateTime lastConnectionAt;

  public Credential(
      HashedPassword hashedPassword,
      HashedIdentifier hashedIdentifier,
      @Nullable SsoInfo ssoInfo,
      @Nullable ToptCode toptCode,
      @Nullable PartialPassKeysInfo passKeysInfo,
      @Nullable ZonedDateTime lastConnectionAt) {
    super();
    this.hashedPassword = hashedPassword;
    this.hashedIdentifier = hashedIdentifier;
    this.ssoInfo = ssoInfo;
    this.toptCode = toptCode;
    this.partialPassKeysInfo = passKeysInfo;
    this.lastConnectionAt = lastConnectionAt;
  }

  protected Credential(
      UUID id,
      HashedPassword hashedPassword,
      HashedIdentifier hashedIdentifier,
      @Nullable SsoInfo ssoInfo,
      @Nullable ToptCode toptCode,
      @Nullable PartialPassKeysInfo passKeysInfo,
      @Nullable ZonedDateTime lastConnectionAt) {
    super(id);
    this.hashedPassword = hashedPassword;
    this.hashedIdentifier = hashedIdentifier;
    this.ssoInfo = ssoInfo;
    this.toptCode = toptCode;
    this.partialPassKeysInfo = passKeysInfo;
    this.lastConnectionAt = lastConnectionAt;
  }

  public static Credential hydrate(
      UUID id,
      HashedPassword hashedPassword,
      HashedIdentifier hashedIdentifier,
      @Nullable SsoInfo ssoInfo,
      @Nullable ToptCode toptCode,
      @Nullable PartialPassKeysInfo passKeysInfo,
      @Nullable ZonedDateTime lastConnectionAt) {
    return new Credential(
        id, hashedPassword, hashedIdentifier, ssoInfo, toptCode, passKeysInfo, lastConnectionAt);
  }

  @Override
  public UUID getId() {
    return id;
  }

  public @Nullable ZonedDateTime getLastConnectionAt() {
    return lastConnectionAt;
  }

  public HashedIdentifier getHashedIdentifier() {
    return hashedIdentifier;
  }

  public HashedPassword getHashedPassword() {
    return hashedPassword;
  }

  public @Nullable SsoInfo getSsoInfo() {
    return ssoInfo;
  }

  public @Nullable ToptCode getToptCode() {
    return toptCode;
  }

  public @Nullable PartialPassKeysInfo getPartialPassKeysInfo() {
    return partialPassKeysInfo;
  }

  @Override
  public ZonedDateTime getCreatedAt() {
    return createdAt;
  }

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof Credential that)) {
      return false;
    }
    return getId().equals(that.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
