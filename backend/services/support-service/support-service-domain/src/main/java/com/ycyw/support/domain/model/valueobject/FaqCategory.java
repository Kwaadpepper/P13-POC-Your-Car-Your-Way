package com.ycyw.support.domain.model.valueobject;

import com.ycyw.shared.utils.Domain;

public record FaqCategory(String value) {
  public FaqCategory {
    Domain.checkDomain(() -> !value.isBlank(), "FAQ category cannot be blank");
  }
}
