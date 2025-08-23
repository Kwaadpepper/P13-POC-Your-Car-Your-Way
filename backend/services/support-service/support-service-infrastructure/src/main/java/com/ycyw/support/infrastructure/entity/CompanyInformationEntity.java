package com.ycyw.support.infrastructure.entity;

import java.util.Map;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.eclipse.jdt.annotation.Nullable;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(schema = "support_context", name = "company_information")
public class CompanyInformationEntity {

  @Id
  @Column(name = "support_email", nullable = false, length = 255)
  private String supportEmail;

  @Column(name = "address_line1", nullable = false, length = 255)
  private String addressLine1;

  @Column(name = "address_line2", length = 255)
  private @Nullable String addressLine2;

  @Column(name = "address_line3", length = 255)
  private @Nullable String addressLine3;

  @Column(name = "address_city", nullable = false, length = 255)
  private String addressCity;

  @Column(name = "address_postcode", nullable = false, length = 255)
  private String addressPostcode;

  @JdbcTypeCode(SqlTypes.CHAR)
  @Column(name = "address_country", nullable = false, length = 2)
  private String addressCountry;

  @Column(name = "support_phone", nullable = false, length = 255)
  private String supportPhone;

  // JSON (Hibernate 6)
  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "support_phone_business_hours", nullable = false, columnDefinition = "json")
  private Map<String, Object> supportPhoneBusinessHours;

  // JSON (Hibernate 6)
  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "support_chat_business_hours", nullable = false, columnDefinition = "json")
  private Map<String, Object> supportChatBusinessHours;

  @Column(name = "website", nullable = false, length = 255)
  private String website;

  public CompanyInformationEntity() {
    this.supportEmail = "";
    this.addressLine1 = "";
    this.addressLine2 = null;
    this.addressLine3 = null;
    this.addressCity = "";
    this.addressPostcode = "";
    this.addressCountry = "";
    this.supportPhone = "";
    this.supportPhoneBusinessHours = java.util.Collections.emptyMap();
    this.supportChatBusinessHours = java.util.Collections.emptyMap();
    this.website = "";
  }

  public String getSupportEmail() {
    return supportEmail;
  }

  public void setSupportEmail(String supportEmail) {
    this.supportEmail = supportEmail;
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

  public String getSupportPhone() {
    return supportPhone;
  }

  public void setSupportPhone(String supportPhone) {
    this.supportPhone = supportPhone;
  }

  public Map<String, Object> getSupportPhoneBusinessHours() {
    return supportPhoneBusinessHours;
  }

  public void setSupportPhoneBusinessHours(Map<String, Object> supportPhoneBusinessHours) {
    this.supportPhoneBusinessHours = supportPhoneBusinessHours;
  }

  public Map<String, Object> getSupportChatBusinessHours() {
    return supportChatBusinessHours;
  }

  public void setSupportChatBusinessHours(Map<String, Object> supportChatBusinessHours) {
    this.supportChatBusinessHours = supportChatBusinessHours;
  }

  public String getWebsite() {
    return website;
  }

  public void setWebsite(String website) {
    this.website = website;
  }

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof CompanyInformationEntity that)) {
      return false;
    }
    return Objects.equals(supportEmail, that.supportEmail);
  }

  @Override
  public int hashCode() {
    return Objects.hash(supportEmail);
  }
}
