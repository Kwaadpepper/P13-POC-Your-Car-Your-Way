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
        () -> extractCategory() != null, "Le code ACRISS comporte une catégorie invalide.");
    Domain.checkDomain(
        () -> extractType() != null, "Le code ACRISS comporte un type de véhicule invalide.");
    Domain.checkDomain(
        () -> extractTransmission() != null,
        "Le code ACRISS comporte un type de transmission invalide.");
    Domain.checkDomain(
        () -> extractFuelAc() != null,
        "Le code ACRISS comporte un type de carburant/climatisation invalide.");
  }

  public AcrissCategory category() {
    return Objects.requireNonNull(extractCategory());
  }

  public AcrissType type() {
    return Objects.requireNonNull(extractType());
  }

  public AcrissTransmission transmission() {
    return Objects.requireNonNull(extractTransmission());
  }

  public AcrissFuelAc fuelAc() {
    return Objects.requireNonNull(extractFuelAc());
  }

  private @Nullable AcrissCategory extractCategory() {
    char code = value.charAt(0);
    return AcrissCategory.fromCode(code);
  }

  private @Nullable AcrissType extractType() {
    char code = value.charAt(1);
    return AcrissType.fromCode(code);
  }

  private @Nullable AcrissTransmission extractTransmission() {
    char code = value.charAt(2);
    return AcrissTransmission.fromCode(code);
  }

  private @Nullable AcrissFuelAc extractFuelAc() {
    char code = value.charAt(3);
    return AcrissFuelAc.fromCode(code);
  }
}
