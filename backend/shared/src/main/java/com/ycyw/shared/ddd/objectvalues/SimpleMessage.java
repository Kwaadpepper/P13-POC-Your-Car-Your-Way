package com.ycyw.shared.ddd.objectvalues;

import com.ycyw.shared.utils.Domain;

public record SimpleMessage(String value) {
  public SimpleMessage {
    Domain.checkDomain(() -> !value.isBlank(), value);
  }
}
