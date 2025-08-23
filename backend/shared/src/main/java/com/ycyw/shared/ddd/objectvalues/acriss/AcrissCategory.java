package com.ycyw.shared.ddd.objectvalues.acriss;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.Nullable;

/** Position 1 du code ACRISS. */
public enum AcrissCategory {
  M('M', "Mini"),
  N('N', "Mini Élite"),
  E('E', "Économique"),
  H('H', "Économique Élite"),
  C('C', "Compacte"),
  D('D', "Compacte / Élite"),
  I('I', "Intermédiaire"),
  J('J', "Intermédiaire Élite"),
  S('S', "Routière"),
  R('R', "Routière / Élite"),
  F('F', "Grande routière"),
  G('G', "Grande routière / Élite"),
  P('P', "Berline Premium"),
  U('U', "Berline Premium Élite"),
  L('L', "Luxe"),
  W('W', "Luxe Élite"),
  O('O', "Très grand gabarit"),
  X('X', "Spécial");

  private final char code;
  private final String description;

  AcrissCategory(char code, String description) {
    this.code = code;
    this.description = description;
  }

  public char code() {
    return code;
  }

  public String description() {
    return description;
  }

  private static final Map<Character, AcrissCategory> CACHE_MAP =
      Arrays.stream(values()).collect(Collectors.toUnmodifiableMap(AcrissCategory::code, v -> v));

  public static @Nullable AcrissCategory fromCode(char code) {
    @Nullable AcrissCategory value = CACHE_MAP.get(code);
    return value;
  }
}
