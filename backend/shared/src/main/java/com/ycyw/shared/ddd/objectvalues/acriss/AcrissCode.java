package com.ycyw.shared.ddd.objectvalues.acriss;

import java.util.Objects;

import com.ycyw.shared.utils.Domain;

import org.eclipse.jdt.annotation.Nullable;

/**
 * Représente un code ACRISS complet (4 caractères: Catégorie, Type, Transmission, Carburant/Clim).
 */
public record AcrissCode(String value) {

  public AcrissCode {
    Domain.checkDomain(
        () -> value.length() == 4, "Un code ACRISS doit comporter exactement 4 caractères.");
    Domain.checkDomain(
        () -> extractCategory(value) != null, "Le code ACRISS comporte une catégorie invalide.");
    Domain.checkDomain(
        () -> extractType(value) != null, "Le code ACRISS comporte un type de véhicule invalide.");
    Domain.checkDomain(
        () -> extractTransmission(value) != null,
        "Le code ACRISS comporte un type de transmission invalide.");
    Domain.checkDomain(
        () -> extractFuelAc(value) != null,
        "Le code ACRISS comporte un type de carburant/climatisation invalide.");
  }

  public AcrissCategory category() {
    return Objects.requireNonNull(extractCategory(value));
  }

  public AcrissType type() {
    return Objects.requireNonNull(extractType(value));
  }

  public AcrissTransmission transmission() {
    return Objects.requireNonNull(extractTransmission(value));
  }

  public AcrissFuelAc fuelAc() {
    return Objects.requireNonNull(extractFuelAc(value));
  }

  private @Nullable AcrissCategory extractCategory(String value) {
    char code = value.charAt(0);
    return AcrissCategory.fromCode(code);
  }

  private @Nullable AcrissType extractType(String value) {
    char code = value.charAt(1);
    return AcrissType.fromCode(code);
  }

  private @Nullable AcrissTransmission extractTransmission(String value) {
    char code = value.charAt(2);
    return AcrissTransmission.fromCode(code);
  }

  private @Nullable AcrissFuelAc extractFuelAc(String value) {
    char code = value.charAt(3);
    return AcrissFuelAc.fromCode(code);
  }
}
