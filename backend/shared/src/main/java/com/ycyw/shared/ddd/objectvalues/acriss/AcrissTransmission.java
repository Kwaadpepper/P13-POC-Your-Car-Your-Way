package com.ycyw.shared.ddd.objectvalues.acriss;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.Nullable;

/** Position 3 du code ACRISS. */
public enum AcrissTransmission {
  M('M', "Manuelle"),
  N('N', "Manuelle (4x4)"),
  C('C', "Manuelle, toutes roues motrices"),
  A('A', "Automatique"),
  B('B', "Automatique (4x4)"),
  D('D', "Automatique, transmission intégrale (AWD)");

  private final char code;
  private final String description;

  AcrissTransmission(char code, String description) {
    this.code = code;
    this.description = description;
  }

  public char code() {
    return code;
  }

  public String description() {
    return description;
  }

  private static final Map<Character, AcrissTransmission> CACHE_MAP =
      Arrays.stream(values())
          .collect(Collectors.toUnmodifiableMap(AcrissTransmission::code, v -> v));

  public static @Nullable AcrissTransmission fromCode(char code) {
    @Nullable AcrissTransmission value = CACHE_MAP.get(code);
    return value;
  }
}
