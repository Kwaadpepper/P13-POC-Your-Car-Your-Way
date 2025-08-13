# Guide : Ajouter un nouveau projet à votre monorepo Angular

Ce guide détaille les étapes pour intégrer proprement un nouveau projet (application ou librairie) dans votre workspace Angular, en respectant la structure, les conventions et l’intégration dans les scripts d’automatisation.

---

## 0. Prérequis repo (post-clone)

Après un clone, exécuter une fois pour activer Husky/commitlint côté frontend :
```bash
cd frontend
npm install
npm run prepare
```
Si besoin, vérifier le chemin des hooks git:
```bash
git config --get core.hooksPath
# doit renvoyer: frontend/.husky
```

---

## 1. Générer le projet Angular

Application :
```bash
ng generate application <nom-du-projet>
```

Librairie :
```bash
ng generate library <nom-de-la-lib>
```

Le projet est créé dans le dossier `projects/`.

---

## 2. Ajouter ESLint au projet

```bash
ng g angular-eslint:add-eslint-to-project <nom-du-projet>
```
- Un fichier `eslint.config.js` (Flat config ESM) est créé dans le dossier du projet.
- Une cible `lint` est ajoutée dans le bloc `architect` du projet dans `angular.json`.

Remarque :
- Depuis Angular 17+, ESLint utilise un fichier de configuration ESM (`eslint.config.js` avec `export default`).
- Si vous souhaitez supprimer le warning Node (MODULE_TYPELESS_PACKAGE_JSON) sans mettre `"type": "module"` à la racine, renommez le fichier en `eslint.config.mjs`.

---

## 3. Générer un composant avec préfixe

Générer un composant :
```bash
ng generate component <nom-du-composant> --project=<nom-du-projet>
```

Le préfixe configuré dans `angular.json` pour le projet sera automatiquement appliqué au selector.

Exemple :
```bash
ng generate component dashboard --project=shell
```
```ts
@Component({
  selector: 'shell-dashboard',
  // ...
})
```

Astuce :
- Pour garantir l’unicité des selectors (utile avec Native Federation), chaque projet doit avoir son propre préfixe, défini dans `angular.json`.
- ESLint vérifie aussi ce préfixe via `@angular-eslint/component-selector`.

---

## 4. Définir les alias TypeScript

- Ouvrez la racine `tsconfig.json`.
- Ajoutez/éditez les alias dans `compilerOptions.paths`.
  - Exemple :
    ```json
    "@mon-alias/*": ["./projects/<nom-du-projet>/src/app/mon-dossier/*"]
    ```
- Ouvrez `projects/<nom-du-projet>/tsconfig.app.json` et ajoutez les alias spécifiques dans `compilerOptions.paths` si nécessaire.

Règles à respecter :
- Les alias globaux (hors `./projects/*`) doivent être présents dans tous les projets.
- Les alias spécifiques à un projet (pointant vers `./projects/<nom-du-projet>`) ne doivent exister que dans le projet concerné.

---

## 5. Importer les styles partagés

Dans le fichier de styles principal du projet (`projects/<nom-du-projet>/src/styles.css` ou `styles.scss`), ajoutez en haut :
```css
@import "~ycyw/styles";
```
Note :
- Le chemin est relatif à `projects/<nom-du-projet>/src/`.
- Si vous utilisez Tailwind, gardez l’ordre d’import attendu par Tailwind (ex : `@import "tailwindcss";` avant ou après selon votre setup).

---

## 6. Installer et utiliser PrimeNG

Installation :
```bash
ng add primeng
```
ou
```bash
npm install primeng primeicons
```

Utilisation :
- Importer les modules nécessaires dans votre projet.
- Documentation : https://primeng.org/

---

## 7. Vérifier les alias

Lancez le script de vérification :
```bash
npm run check:alias
```
Corrigez toutes les erreurs jusqu’à obtention d’un message de succès.

---

## 8. Lancer le lint sur le projet

```bash
ng lint <nom-du-projet>
```

Lint global :
```bash
npm run lint
```

---

## 9. Vérifier la build

Application :
```bash
ng build <nom-du-projet>
```

Build globale :
```bash
npm run build
```

---

## 10. Adapter la configuration spécifique (Native Federation, styles, etc.)

- Si le projet utilise Native Federation, configurez le builder dans `angular.json` conformément aux exemples existants.
- Ajoutez les dépendances/configurations spécifiques (stylelint, tailwind, etc.) selon les besoins.

Note Native Federation (important) :
- Les fichiers de configuration de federation doivent rester en CommonJS (CJS). Utilisez `.cjs` (ex. `federation.config.cjs`).
- Ne convertissez pas ces fichiers en ESM.

### 10.1 Mettre à jour la configuration Native Federation (obligatoire)

Afin d’uniformiser le partage des dépendances et d’éviter les doublons, chaque projet doit s’appuyer sur une configuration partagée.

1) Fichier partagé (centralisé, en CJS) :
- `frontend/shared/federation-shared.config.cjs`

2) Configuration de chaque projet (fichier obligatoire et codé en dur) :
- `projects/<nom-du-projet>/federation.config.js` (CommonJS, même si l’extension est `.js`)

Contenu type :
```js
const { withNativeFederation } = require('@angular-architects/native-federation/config');
const federationShared = require('../../federation-shared.config.cjs');

module.exports = withNativeFederation({
  shared: { ...federationShared.shared },
  skip: [ ...federationShared.skip ],
  features: { ignoreUnusedDeps: true }
});
```

Points d’attention :
- Le fichier du projet doit s’appeler exactement `federation.config.js`.
- Conservez la syntaxe CommonJS (`require`/`module.exports`).
- Ne définissez pas `"type": "module"` dans le package du frontend, sinon `require` ne sera plus disponible dans les fichiers `.js`.
- Après modification, validez avec :
  ```bash
  ng build <nom-du-projet>
  ```

---

## 11. Configurer les environnements Angular

Pour gérer des variables d’environnement (`production`, `development`, etc.) propres à chaque application, Angular utilise `fileReplacements` lors de la build.

### Génération des fichiers d’environnement
```bash
ng generate environments --project=<nom-du-projet>
```
⚠️ La structure générée peut varier selon les versions Angular. Vérifiez et ajustez l’emplacement.

### Structure recommandée
```
projects/<nom-du-projet>/src/environments/
```

Fichiers :
- `environment.ts` (valeurs par défaut / production)
- `environment.development.ts` (valeurs spécifiques à l’environnement dev)

Exemples :
```typescript name=projects/support-app/src/environments/environment.ts
export const environment = {
  production: true,
  apiUrl: 'https://api.monsite.com'
};
```
```typescript name=projects/support-app/src/environments/environment.development.ts
export const environment = {
  production: false,
  apiUrl: 'http://localhost:3000'
};
```

### Configuration dans `angular.json`
Sous `architect > esbuild > configurations > development` :
```json
"fileReplacements": [
  {
    "replace": "projects/<nom-du-projet>/src/environments/environment.ts",
    "with": "projects/<nom-du-projet>/src/environments/environment.development.ts"
  }
]
```
⚠️ Placez `fileReplacements` dans `esbuild > configurations > development` (pas dans le builder federation).

Exemple pour `support-app` :
```json
"esbuild": {
  "builder": "@angular/build:application",
  "options": { ... },
  "configurations": {
    "production": { ... },
    "development": {
      "optimization": false,
      "extractLicenses": false,
      "sourceMap": true,
      "fileReplacements": [
        {
          "replace": "projects/support-app/src/environments/environment.ts",
          "with": "projects/support-app/src/environments/environment.development.ts"
        }
      ]
    }
  },
  "defaultConfiguration": "production"
}
```

### Utilisation dans le code
```ts
import { environment } from 'src/environments/environment';

if (environment.production) {
  // code spécifique à la prod
}
```

Bonnes pratiques :
- Ne stockez jamais de secrets (build côté client).
- Les valeurs d’environnement doivent rester publiques/non sensibles.

---

## 12. Conventional Commits (scopes)

Ajoutez/maintenez les scopes dans `.vscode/settings.json` (utilisé par l’extension `vivaxy.vscode-conventional-commits` et lu par commitlint) :
```json
"conventionalCommits.scopes": [
  "frontend",
  "frontend-users",
  "frontend-shell",
  "frontend-shared",
  "<nom-du-nouveau-projet>"
]
```

Exemples de messages valides :
- `feat(frontend-shell): ajoute le menu latéral`
- `fix(frontend-shared): corrige l’ordre des imports`
- `chore(frontend): configure husky et lint-staged`

---

## 13. Commit & Push

```bash
git add .
git commit -m "feat(<scope>): ajout du projet <nom-du-projet>"
git push
```

---

## Checklist rapide

- [ ] Projet généré dans `projects/`
- [ ] ESLint installé et configuré (ESM ou `.mjs` si souhaité)
- [ ] Composants générés avec un préfixe unique
- [ ] Alias TypeScript définis et valides
- [ ] Styles partagés importés
- [ ] PrimeNG installé et utilisé
- [ ] Native Federation branchée sur la config partagée (CJS)
- [ ] Fichiers d’environnement créés et fileReplacements configurés
- [ ] Lint OK
- [ ] Build OK
- [ ] Scopes Conventional Commits mis à jour dans `.vscode/settings.json`
- [ ] Commit effectué
