package com.ycyw.shared.ddd.objectvalues.acriss;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.Nullable;

/** Position 4 du code ACRISS (Carburant / Climatisation). */
public enum AcrissFuelAc {
  R('R', "Non spécifié, avec climatisation"),
  N('N', "Non spécifié, sans climatisation"),
  D('D', "Diesel, avec climatisation"),
  Q('Q', "Diesel, sans climatisation"),
  H('H', "Hybride, avec climatisation"),
  I('I', "Véhicule plugin, avec climatisation"),
  E('E', "Électrique, autonomie < 400 kilomètres"),
  C('C', "Électrique, autonomie > 400 kilomètres"),
  L('L', "GPL, avec climatisation"),
  S('S', "GPL, sans climatisation"),
  A('A', "Hydrogène, avec climatisation"),
  B('B', "Hydrogène, sans climatisation"),
  M('M', "Flexible (multi-carburants), avec climatisation"),
  F('F', "Flexible (multi-carburants), sans climatisation"),
  V('V', "Essence, avec climatisation"),
  Z('Z', "Essence, sans climatisation"),
  U('U', "Éthanol, avec climatisation"),
  X('X', "Éthanol, sans climatisation");

  private final char code;
  private final String description;

  AcrissFuelAc(char code, String description) {
    this.code = code;
    this.description = description;
  }

  public char code() {
    return code;
  }

  public String description() {
    return description;
  }

  private static final Map<Character, AcrissFuelAc> CACHE_MAP =
      Arrays.stream(values()).collect(Collectors.toUnmodifiableMap(AcrissFuelAc::code, v -> v));

  public static @Nullable AcrissFuelAc fromCode(char code) {
    @Nullable AcrissFuelAc value = CACHE_MAP.get(code);
    return value;
  }
}
