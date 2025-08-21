package com.ycyw.users.domain.model.valueobject;

import com.ycyw.shared.ddd.exceptions.IllegalDomainStateException;

public enum SsoProvider {
  GOOGLE(0),
  MICROSOFT(1),
  APPLE(2);

  private final int cardinality;

  SsoProvider(int cardinality) {
    this.cardinality = cardinality;
  }

  public int getCardinality() {
    return cardinality;
  }

  public static SsoProvider fromCardinality(int cardinality) {
    for (SsoProvider provider : SsoProvider.values()) {
      if (provider.getCardinality() == cardinality) {
        return provider;
      }
    }
    throw new IllegalDomainStateException("Invalid SsoProvider cardinality: " + cardinality);
  }
}
