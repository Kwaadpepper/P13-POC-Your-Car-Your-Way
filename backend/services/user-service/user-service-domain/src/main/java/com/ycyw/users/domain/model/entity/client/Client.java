package com.ycyw.users.domain.model.entity.client;

import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

import com.ycyw.shared.ddd.lib.AggregateRoot;
import com.ycyw.shared.utils.Domain;
import com.ycyw.users.domain.model.entity.credential.CredentialId;
import com.ycyw.users.domain.model.valueobject.Address;
import com.ycyw.users.domain.model.valueobject.BirthDate;
import com.ycyw.users.domain.model.valueobject.Email;

import org.eclipse.jdt.annotation.Nullable;

public class Client extends AggregateRoot {
  private final String lastName;
  private final String firstName;
  private final Email email;
  private final String phone;
  private final BirthDate birthDate;
  private final Address address;
  private final CredentialId credentialId;
  private final ZonedDateTime updatedAt;
  private final @Nullable ZonedDateTime deletedAt;

  private static void validateCommonInvariants(String lastName, String firstName) {
    Domain.checkDomain(() -> !lastName.isBlank(), "Last name cannot be blank");
    Domain.checkDomain(() -> !firstName.isBlank(), "First name cannot be blank");
  }

  public Client(
      String lastName,
      String firstName,
      Email email,
      String phone,
      BirthDate birthDate,
      Address address,
      CredentialId credentialId,
      ZonedDateTime updatedAt,
      @Nullable ZonedDateTime deletedAt) {
    super();
    validateCommonInvariants(lastName, firstName);
    this.lastName = lastName;
    this.firstName = firstName;
    this.email = email;
    this.phone = phone;
    this.birthDate = birthDate;
    this.address = address;
    this.credentialId = credentialId;
    this.updatedAt = updatedAt;
    this.deletedAt = deletedAt;
  }

  protected Client(
      UUID id,
      String lastName,
      String firstName,
      Email email,
      String phone,
      BirthDate birthDate,
      Address address,
      CredentialId credentialId,
      ZonedDateTime updatedAt,
      @Nullable ZonedDateTime deletedAt) {
    super(id);
    validateCommonInvariants(lastName, firstName);
    this.lastName = lastName;
    this.firstName = firstName;
    this.email = email;
    this.phone = phone;
    this.birthDate = birthDate;
    this.address = address;
    this.credentialId = credentialId;
    this.updatedAt = updatedAt;
    this.deletedAt = deletedAt;
  }

  public static Client hydrate(
      UUID id,
      String lastName,
      String firstName,
      Email email,
      String phone,
      BirthDate birthDate,
      Address address,
      CredentialId credentialId,
      ZonedDateTime updatedAt,
      @Nullable ZonedDateTime deletedAt) {
    return new Client(
        id,
        lastName,
        firstName,
        email,
        phone,
        birthDate,
        address,
        credentialId,
        updatedAt,
        deletedAt);
  }

  @Override
  public UUID getId() {
    return id;
  }

  public String getLastName() {
    return lastName;
  }

  public String getFirstName() {
    return firstName;
  }

  public Email getEmail() {
    return email;
  }

  public String getPhone() {
    return phone;
  }

  public BirthDate getBirthDate() {
    return birthDate;
  }

  public Address getAddress() {
    return address;
  }

  public CredentialId getCredentialId() {
    return credentialId;
  }

  @Override
  public ZonedDateTime getCreatedAt() {
    return createdAt;
  }

  public ZonedDateTime getUpdatedAt() {
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
    if (!(obj instanceof Client that)) {
      return false;
    }
    return getId().equals(that.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
