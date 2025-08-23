package com.ycyw.shared.ddd.objectvalues;

import com.ycyw.shared.utils.Domain;

public record Coordinates(Double lat, Double lng) {
  public Coordinates {
    Domain.checkDomain(() -> lat >= -90.0 && lat <= 90.0, "Latitude must be between -90 and 90");
    Domain.checkDomain(
        () -> lng >= -180.0 && lng <= 180.0, "Longitude must be between -180 and 180");
  }
}
