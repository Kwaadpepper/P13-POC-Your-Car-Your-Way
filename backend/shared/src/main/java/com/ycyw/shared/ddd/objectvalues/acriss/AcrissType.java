package com.ycyw.shared.ddd.objectvalues.acriss;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.Nullable;

/** Position 2 du code ACRISS. */
public enum AcrissType {
  B('B', "2-3 portes"),
  C('C', "2-4 portes"),
  D('D', "4-5 portes"),
  W('W', "Break"),
  V('V', "Minibus - plus de 6 passagers"),
  L('L', "Limousine / sedan"),
  S('S', "Sport"),
  T('T', "Cabriolet"),
  F('F', "SUV"),
  J('J', "Tout terrain"),
  X('X', "Spéciale"),
  P('P', "Pick-up (cabine simple 2 portes)"),
  Q('Q', "Pick-up (cabine double 4 portes)"),
  Z('Z', "Offre spéciale"),
  E('E', "Coupé"),
  M('M', "Monospace 5 passagers max"),
  R('R', "Véhicule de loisirs"),
  H('H', "Camping-car"),
  Y('Y', "Deux roues motorisées"),
  N('N', "Roadster"),
  G('G', "Crossover"),
  K('K', "Véhicule utilitaire");

  private final char code;
  private final String description;

  AcrissType(char code, String description) {
    this.code = code;
    this.description = description;
  }

  public char code() {
    return code;
  }

  public String description() {
    return description;
  }

  private static final Map<Character, AcrissType> CACHE_MAP =
      Arrays.stream(values()).collect(Collectors.toUnmodifiableMap(AcrissType::code, v -> v));

  public static @Nullable AcrissType fromCode(char code) {
    @Nullable AcrissType value = CACHE_MAP.get(code);
    return value;
  }
}
