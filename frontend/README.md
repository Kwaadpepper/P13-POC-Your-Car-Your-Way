# Frontend

Ce projet est un monorepo Angular (version 20) orienté micro-frontends, conçu pour l’authentification et le support dans une architecture scalable.

---

## 1. Prérequis

- **Node.js** (>=18 recommandé)
- **bun** (optionnel mais recommandé) [https://bun.sh/](https://bun.sh/)
- Un navigateur moderne (Chrome, Firefox, Edge…)

---

## 2. Architecture du projet

- Le dossier `projects/` contient les différentes applications et librairies.
- Les styles partagés sont mutualisés dans `shared/src/styles/shared.css`.
- Chaque projet possède son propre préfixe pour les composants.
- Les conventions de nommage et d’organisation facilitent l’intégration de micro-frontends (Native Federation).

---

## 3. Configuration / Variables d’environnement

Les variables d’environnement sont gérées via les fichiers `environment.*.ts` dans chaque projet.
Pour personnaliser la configuration :
- Modifiez les fichiers `projects/<nom-du-projet>/src/environments/`.
- Exemple : pour changer l’URL de l’API, éditez `environment.ts`.

---

## 4. Gestion des styles

Dans chaque projet, le fichier de styles principal doit importer les styles partagés :

```css
@import "./../../../shared/src/styles/shared.css";
```

Cela permet d’uniformiser l’apparence et d’éviter la duplication.

---

## 5. Convention de nommage

Chaque projet Angular définit un préfixe unique dans `angular.json`.
Tous les tags de composants générés utilisent ce préfixe (ex : `shell-dashboard`, `support-dashboard`), ce qui évite les collisions lors de l’utilisation de Native Federation.

---

## 6. Bonnes pratiques Angular & TypeScript

- Utilisez **standalone components** (pas de NgModules).
- Utilisez **signals** pour le state local.
- Préférez les **composants petits et spécialisés**.
- Utilisez `input()` et `output()` au lieu des décorateurs.
- Préférez **Reactive Forms**.
- Utilisez les **structures de contrôle natives** (`@if`, `@for`, `@switch`).
- Utilisez `NgOptimizedImage` pour les images statiques.
- Respectez la **single responsibility** pour les services.
- Utilisez `inject()` au lieu de l’injection par constructeur.
- Évitez `ngClass`/`ngStyle`, utilisez des bindings `class`/`style`.
- Activez `changeDetection: ChangeDetectionStrategy.OnPush` dans les composants.
- Privilégiez le typage strict TypeScript, évitez le type `any`.

---

## 7. Utilisation des librairies

La bibliothèque principale est **PrimeNG**.

**Installation :**
```bash
ng add primeng
# ou
npm install primeng primeicons
# ou
bun add primeng primeicons
```

Importez les modules nécessaires selon la [documentation officielle PrimeNG](https://primeng.org/).

---

## 8. Scripts d’automatisation

Des scripts personnalisés (alias, formatage, vérifications) sont en cours d’ajout dans le dossier `scripts/`.  
Consultez les scripts disponibles dans `package.json`.

---

## 9. Déploiement

Les artefacts de build sont générés dans le dossier `dist/`.

**Déploiement local :**
- Compiler en production :
  ```bash
  npm run build --project=shell
  # ou
  bun run build --project=shell
  ```
- Copier le contenu de `dist/<nom-du-projet>` sur le serveur cible.

**Automatisation** : à venir (scripts et CI/CD).

---

## 10. Contribution

- Forkez le dépôt et créez une branche dédiée (`feature/<votre-nom>/<description>`).
- Respectez la convention de nommage et les bonnes pratiques Angular/TypeScript.
- Décrivez clairement vos Pull Requests.
- Vérifiez le lint et les tests avant toute PR.

---

## 🖥️ Développement local

Pour lancer le serveur de développement sur un projet spécifique :

```bash
npm run start --project=shell
# ou
bun run start --project=shell
```

Ouvrez [http://localhost:4200/](http://localhost:4200/).

---

## 🧹 Linter le code

Pour lancer le lint sur un projet précis :
```bash
ng lint <nom-du-projet>
```

Pour tous les projets :
```bash
npm run lint
# ou
bun run lint
```

---

## ⚙️ Construire le projet

Pour compiler en production :
```bash
npm run build --project=shell
# ou
bun run build --project=shell
```

Pour tous les projets :
```bash
npm run build
# ou
bun run build
```

Les artefacts se trouvent dans `dist/`.

---

## 👁️ Build en mode watch

Pour builder en mode « watch » :
```bash
npm run watch --project=shell
# ou
bun run watch --project=shell
```

---

## 🧪 Tester

Pour exécuter les tests unitaires :
```bash
npm run test --project=shell
# ou
bun run test --project=shell
```

---

## 📚 Guide d’ajout de projet

Consultez le guide détaillé :
➡️ [GUIDE-ajouter-un-nouveau-projet-angular-monorepo.md](./GUIDE-ajouter-un-nouveau-projet-angular-monorepo.md)

---

## 🔗 Ressources complémentaires

- [Documentation Angular CLI](https://angular.dev/tools/cli)
- [Documentation PrimeNG](https://primeng.org/)
