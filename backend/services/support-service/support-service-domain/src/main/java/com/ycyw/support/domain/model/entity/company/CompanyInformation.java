package com.ycyw.support.domain.model.entity.company;

import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

import com.ycyw.shared.ddd.lib.AggregateRoot;
import com.ycyw.shared.ddd.objectvalues.Address;
import com.ycyw.shared.ddd.objectvalues.Email;
import com.ycyw.shared.ddd.objectvalues.HttpUrl;
import com.ycyw.shared.ddd.objectvalues.PhoneNumber;
import com.ycyw.support.domain.model.valueobject.BusinessHours;

import org.eclipse.jdt.annotation.Nullable;

public class CompanyInformation extends AggregateRoot {
  private final Address companyAddress;
  private final PhoneNumber phoneSupport;
  private final BusinessHours phoneSupportBusinessHours;
  private final BusinessHours chatSupportBusinessHours;
  private final Email emailSupport;
  private final HttpUrl website;

  public CompanyInformation(
      Address companyAddress,
      PhoneNumber phoneSupport,
      BusinessHours phoneSupportBusinessHours,
      BusinessHours chatSupportBusinessHours,
      Email emailSupport,
      HttpUrl website) {
    super();
    this.companyAddress = companyAddress;
    this.phoneSupport = phoneSupport;
    this.phoneSupportBusinessHours = phoneSupportBusinessHours;
    this.chatSupportBusinessHours = chatSupportBusinessHours;
    this.emailSupport = emailSupport;
    this.website = website;
  }

  protected CompanyInformation(
      UUID id,
      Address companyAddress,
      PhoneNumber phoneSupport,
      BusinessHours phoneSupportBusinessHours,
      BusinessHours chatSupportBusinessHours,
      Email emailSupport,
      HttpUrl website) {
    super(id);
    this.companyAddress = companyAddress;
    this.phoneSupport = phoneSupport;
    this.phoneSupportBusinessHours = phoneSupportBusinessHours;
    this.chatSupportBusinessHours = chatSupportBusinessHours;
    this.emailSupport = emailSupport;
    this.website = website;
  }

  public static CompanyInformation hydrate(
      UUID id,
      Address companyAddress,
      PhoneNumber phoneSupport,
      BusinessHours phoneSupportBusinessHours,
      BusinessHours chatSupportBusinessHours,
      Email emailSupport,
      HttpUrl website) {
    return new CompanyInformation(
        id,
        companyAddress,
        phoneSupport,
        phoneSupportBusinessHours,
        chatSupportBusinessHours,
        emailSupport,
        website);
  }

  @Override
  public UUID getId() {
    return id;
  }

  public Address getCompanyAddress() {
    return companyAddress;
  }

  public PhoneNumber getPhoneSupport() {
    return phoneSupport;
  }

  public BusinessHours getPhoneSupportBusinessHours() {
    return phoneSupportBusinessHours;
  }

  public BusinessHours getChatSupportBusinessHours() {
    return chatSupportBusinessHours;
  }

  public Email getEmailSupport() {
    return emailSupport;
  }

  public HttpUrl getWebsite() {
    return website;
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
    if (!(obj instanceof CompanyInformation that)) {
      return false;
    }
    return getId().equals(that.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
