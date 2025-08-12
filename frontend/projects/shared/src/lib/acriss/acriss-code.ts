import { AcrissCategory, AcrissFuelAC, AcrissTransmission, AcrissType } from './enums'

// Mapping tables for descriptions
const ACRISS_CATEGORY_DESCRIPTIONS: Record<AcrissCategory, string> = {
  M: 'Mini',
  N: 'Mini Élite',
  E: 'Économique',
  H: 'Économique Élite',
  C: 'Compacte',
  D: 'Compacte / Élite',
  I: 'Intermédiaire',
  J: 'Intermédiaire Élite',
  S: 'Routière',
  R: 'Routière / Élite',
  F: 'Grande routière',
  G: 'Grande routière / Élite',
  P: 'Berline Premium',
  U: 'Berline Premium Élite',
  L: 'Luxe',
  W: 'Luxe Élite',
  O: 'Très grand gabarit',
  X: 'Spécial',
}

const ACRISS_TYPE_DESCRIPTIONS: Record<AcrissType, string> = {
  B: '2-3 portes',
  C: '2-4 portes',
  D: '4-5 portes',
  W: 'Break',
  V: 'Minubus - plus de 6 passagers',
  L: 'Limousine / sedan',
  S: 'Sport',
  T: 'Cabriolet',
  F: 'SUV',
  J: 'Tout terrain',
  X: 'Spéciale',
  P: 'Pick-up (cabine simple 2 portes)',
  Q: 'Pick-up (cabine double 4 portes)',
  Z: 'Offre spéciale',
  E: 'Coupé',
  M: 'Monospace 5 passagers max',
  R: 'Véhicule de loisirs',
  H: 'Camping-car',
  Y: 'Deux roues motorisées',
  N: 'Roadster',
  G: 'Crossover',
  K: 'Véhicule utilitaire',
}

const ACRISS_TRANSMISSION_DESCRIPTIONS: Record<AcrissTransmission, string> = {
  M: 'Manuelle',
  N: 'Manuelle (4x4)',
  C: 'Manuelle, toutes roues motrices',
  A: 'Automatique',
  B: 'Automatique (4x4)',
  D: 'Automatique, transmission intégrale (AWD)',
}

const ACRISS_FUEL_AC_DESCRIPTIONS: Record<AcrissFuelAC, string> = {
  R: 'Non spécifié, avec climatisation',
  N: 'Non spécifié, sans climatisation',
  D: 'Diesel, avec climatisation',
  Q: 'Diesel, sans climatisation',
  H: 'Hybride, avec climatisation',
  I: 'Véhicule plugin, avec climatisation',
  E: 'Électrique, autonomie < 400 kilomètres',
  C: 'Électrique, autonomie > 400 kilomètres',
  L: 'GPL, avec climatisation',
  S: 'GPL, sans climatisation',
  A: 'Hydrogène, avec climatisation',
  B: 'Hydrogène, sans climatisation',
  M: 'Flexible (multi-carburants), avec climatisation',
  F: 'Flexible (multi-carburants), sans climatisation',
  V: 'Essence, avec climatisation',
  Z: 'Essence, sans climatisation',
  U: 'Éthanol, avec climatisation',
  X: 'Éthanol, sans climatisation',
}

export interface DecodedAcrissInfo {
  categorie: { description: string, value: AcrissCategory }
  type: { description: string, value: AcrissType }
  transmissionEtRouesMotrices: { description: string, value: AcrissTransmission }
  carburantEtClimatisation: { description: string, value: AcrissFuelAC }
}

/**
 * Check if is enum keys.
 */
function isEnumKey<E extends Record<string, string>>(e: E, k: string): k is Extract<keyof E, string> {
  return Object.hasOwn(e, k)
}

class AcrissDecoder {
  /**
   * Decode an ACRISS code
   * @throws Error if the code is invalid.
   */
  public static decode(code: string): DecodedAcrissInfo {
    if (!code || code.length !== 4) {
      throw new Error('ACRISS code must be a 4-character string.')
    }

    const upperCode = code.toUpperCase()
    const [catChar, typeChar, transChar, fuelChar] = upperCode

    if (!isEnumKey(AcrissCategory, catChar)
      || !isEnumKey(AcrissType, typeChar)
      || !isEnumKey(AcrissTransmission, transChar)
      || !isEnumKey(AcrissFuelAC, fuelChar)) {
      const invalidParts: string[] = []
      if (!isEnumKey(AcrissCategory, catChar)) invalidParts.push(`category ('${catChar}')`)
      if (!isEnumKey(AcrissType, typeChar)) invalidParts.push(`type ('${typeChar}')`)
      if (!isEnumKey(AcrissTransmission, transChar)) invalidParts.push(`transmission ('${transChar}')`)
      if (!isEnumKey(AcrissFuelAC, fuelChar)) invalidParts.push(`fuel/AC ('${fuelChar}')`)

      throw new Error(`Invalid ACRISS code "${upperCode}" Invalid parts: ${invalidParts.join(', ')}.`)
    }

    return {
      categorie: {
        description: ACRISS_CATEGORY_DESCRIPTIONS[catChar],
        value: AcrissCategory[catChar],
      },
      type: {
        description: ACRISS_TYPE_DESCRIPTIONS[typeChar],
        value: AcrissType[typeChar],
      },
      transmissionEtRouesMotrices: {
        description: ACRISS_TRANSMISSION_DESCRIPTIONS[transChar],
        value: AcrissTransmission[transChar],
      },
      carburantEtClimatisation: {
        description: ACRISS_FUEL_AC_DESCRIPTIONS[fuelChar],
        value: AcrissFuelAC[fuelChar],
      },
    }
  }

  public static encode(
    category: AcrissCategory,
    type: AcrissType,
    transmission: AcrissTransmission,
    fuelAC: AcrissFuelAC,
  ): string {
    return `${category}${type}${transmission}${fuelAC}`.toUpperCase()
  }
}

export class AcrissCode {
  constructor(
    public readonly category: AcrissCategory,
    public readonly type: AcrissType,
    public readonly transmission: AcrissTransmission,
    public readonly fuelAC: AcrissFuelAC,
  ) { }

  public static fromCode(code: string): AcrissCode {
    const decoded = AcrissDecoder.decode(code)
    return new AcrissCode(
      decoded.categorie.value,
      decoded.type.value,
      decoded.transmissionEtRouesMotrices.value,
      decoded.carburantEtClimatisation.value,
    )
  }

  public get code(): string {
    return AcrissDecoder.encode(this.category, this.type, this.transmission, this.fuelAC)
  }

  public get decoded(): DecodedAcrissInfo {
    return AcrissDecoder.decode(this.code)
  }

  public get categoryLabel(): string {
    return ACRISS_CATEGORY_DESCRIPTIONS[this.category]
  }

  public get typeLabel(): string {
    return ACRISS_TYPE_DESCRIPTIONS[this.type]
  }

  public get transmissionLabel(): string {
    return ACRISS_TRANSMISSION_DESCRIPTIONS[this.transmission]
  }

  public get fuelACLabel(): string {
    return ACRISS_FUEL_AC_DESCRIPTIONS[this.fuelAC]
  }

  public toString(): string {
    return this.code
  }
}
