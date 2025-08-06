# Guide : Utilisation des alias TypeScript avec Angular Native Federation

## TL;DR

**Ne pas utiliser d’alias TypeScript (`paths` dans le tsconfig) pour les imports dans le code partagé ou exposé via Native Federation.  
Utilisez toujours des imports relatifs pour garantir la compatibilité avec le build fédéré.**

---

## 1. Alias TypeScript : principe

Les alias TypeScript permettent d’écrire des imports plus lisibles :
```json
"paths": {
  "@domain/*": ["src/app/domain/*"]
}
```
```typescript
import { SuperLabel } from '@domain/tchat/components/super-label/super-label'
```

---

## 2. Problème avec Native Federation

- Native Federation utilise Webpack/esbuild pour le build fédéré.
- Ces outils **ne lisent pas** la configuration d’alias du tsconfig.
- Les alias TypeScript ne sont compris que par TypeScript, pas par le bundler.
- Résultat : le build échoue avec des erreurs de type :
  ```
  Could not resolve "@domain/tchat/components/super-label/super-label"
  ```
- Il n’existe pas de solution simple ou stable pour synchroniser les alias TypeScript et la résolution Webpack dans un projet Angular CLI + Native Federation.

---

## 3. Bonnes pratiques

### A. Pour le code non fédéré (privé à une seule app)
- Les alias TypeScript sont utilisables dans les fichiers qui ne sont ni exposés ni partagés via Native Federation.

### B. Pour le code fédéré (exposé ou partagé via Native Federation)
- **Toujours utiliser des imports relatifs** :
  ```typescript
  // ⚠️ Alias TypeScript interdit ici
  import { SuperLabel } from '@domain/tchat/components/super-label/super-label'

  // ✅ Correct
  import { SuperLabel } from './../../domain/tchat/components/super-label/super-label'
  ```
- Cela concerne :
  - Les fichiers référencés dans la config federation (`exposes`, `shared`)
  - Les routes, modules ou composants partagés entre microfrontends

### C. Utilisez des fichiers `index.ts` pour simplifier les imports relatifs

Pour éviter des chemins trop longs ou complexes, ajoutez des fichiers `index.ts` dans chaque dossier :
```typescript
// src/app/domain/tchat/components/super-label/index.ts
export * from './super-label';
```
Ainsi, vos imports relatifs restent courts :
```typescript
import { SuperLabel } from './../../domain/tchat/components/super-label';
```

---

## 4. Règle d’or

> **Dans tous les modules exposés ou partagés via Native Federation,  
> n’utilisez JAMAIS les alias TypeScript.  
> Utilisez systématiquement les imports relatifs, idéalement via des index.ts.**

---

## 5. FAQ

**Q : Peut-on synchroniser les alias TypeScript avec Webpack dans Angular CLI ?**  
R : Non, ce n’est pas supporté nativement. Les plugins existants sont instables et non recommandés en production.

**Q : Est-ce que les alias TypeScript fonctionnent dans les tests ?**  
R : Oui, tant que ces fichiers ne sont pas exposés ou partagés via la federation.

**Q : Que faire si j’ai une erreur "Could not resolve '@domain/...' ?"**  
R : Remplace l’import par un chemin relatif dans ce fichier.

---

## 6. Ressources

- [Angular Architects Native Federation FAQ](https://www.angulararchitects.io/aktuelles/native-federation-faq/)
- [Guide officiel TypeScript paths](https://www.typescriptlang.org/tsconfig#paths)

---

## 7. Pour aller plus loin

Pour toute question, contactez l’équipe d’architecture ou votre référent frontend.
