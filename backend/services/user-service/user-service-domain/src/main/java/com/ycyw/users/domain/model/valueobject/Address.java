package com.ycyw.users.domain.model.valueobject;

import com.ycyw.shared.ddd.objectvalues.Country;
import com.ycyw.shared.utils.Domain;

import org.eclipse.jdt.annotation.Nullable;

public record Address(
    String line1,
    @Nullable String line2,
    @Nullable String line3,
    String city,
    String zipCode,
    Country country) {
  public Address {
    Domain.checkDomain(() -> !line1.isBlank(), "Address line 1 cannot be blank");
    Domain.checkDomain(
        () -> (line2 == null || !line2.isBlank()), "Address line 2 cannot be blank if provided");
    Domain.checkDomain(
        () -> (line3 == null || !line3.isBlank()), "Address line 3 cannot be blank if provided");
    Domain.checkDomain(() -> !city.isBlank(), "City cannot be blank");
    Domain.checkDomain(() -> !zipCode.isBlank(), "Zip code cannot be blank");
  }
}
