package com.ycyw.users.infrastructure.entity;

import java.time.LocalDate;
import java.time.ZoneId;
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
@Table(schema = "user_context", name = "clients")
public class ClientEntity {

  @Id
  @Column(columnDefinition = "uuid")
  private UUID id;

  @Column(name = "last_name", nullable = false, length = 255)
  private String lastName;

  @Column(name = "first_name", nullable = false, length = 255)
  private String firstName;

  @Column(nullable = false, length = 255, unique = true)
  private String email;

  @Column(nullable = false, length = 255)
  private String phone;

  @Column(nullable = false, length = 255)
  private LocalDate birthdate;

  @Column(name = "address_line1", nullable = false, length = 255)
  private String addressLine1;

  @Column(name = "address_line2", length = 255, nullable = true)
  @Nullable private String addressLine2;

  @Column(name = "address_line3", length = 255, nullable = true)
  @Nullable private String addressLine3;

  @Column(name = "address_city", nullable = false, length = 255)
  private String addressCity;

  @Column(name = "address_postcode", nullable = false, length = 255)
  private String addressPostcode;

  @JdbcTypeCode(SqlTypes.CHAR)
  @Column(name = "address_country", nullable = false, length = 2)
  private String addressCountry;

  // Référence d'agrégat : stocke l'identifiant UUID de Credential (pas de relation JPA)
  @Column(name = "credential", columnDefinition = "uuid", unique = true, nullable = false)
  private UUID credentialId;

  @Column(name = "updated_at", nullable = false)
  private ZonedDateTime updatedAt;

  @Column(name = "deleted_at", nullable = true)
  @Nullable private ZonedDateTime deletedAt;

  public ClientEntity() {
    // JPA requires a no-arg constructor
    this.id = UUID.randomUUID();
    this.lastName = "";
    this.firstName = "";
    this.email = "";
    this.phone = "";
    this.birthdate = LocalDate.now(ZoneId.systemDefault());
    this.addressLine1 = "";
    this.addressLine2 = null;
    this.addressLine3 = null;
    this.addressCity = "";
    this.addressPostcode = "";
    this.addressCountry = "";
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

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public LocalDate getBirthdate() {
    return birthdate;
  }

  public void setBirthdate(LocalDate birthdate) {
    this.birthdate = birthdate;
  }

  public String getAddressLine1() {
    return addressLine1;
  }

  public void setAddressLine1(String addressLine1) {
    this.addressLine1 = addressLine1;
  }

  public @Nullable String getAddressLine2() {
    return addressLine2;
  }

  public void setAddressLine2(@Nullable String addressLine2) {
    this.addressLine2 = addressLine2;
  }

  public @Nullable String getAddressLine3() {
    return addressLine3;
  }

  public void setAddressLine3(@Nullable String addressLine3) {
    this.addressLine3 = addressLine3;
  }

  public String getAddressCity() {
    return addressCity;
  }

  public void setAddressCity(String addressCity) {
    this.addressCity = addressCity;
  }

  public String getAddressPostcode() {
    return addressPostcode;
  }

  public void setAddressPostcode(String addressPostcode) {
    this.addressPostcode = addressPostcode;
  }

  public String getAddressCountry() {
    return addressCountry;
  }

  public void setAddressCountry(String addressCountry) {
    this.addressCountry = addressCountry;
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
    if (!(obj instanceof ClientEntity that)) {
      return false;
    }
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
