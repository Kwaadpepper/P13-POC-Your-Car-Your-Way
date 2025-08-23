package com.ycyw.shared.ddd.objectvalues;

import java.net.URI;

import com.ycyw.shared.utils.Domain;

public record HttpUrl(URI value) {
  public HttpUrl {
    Domain.checkDomain(
        () -> {
          final var s = value.getScheme();
          return "http".equalsIgnoreCase(s) || "https".equalsIgnoreCase(s);
        },
        "URL must use http or https");
  }
}
