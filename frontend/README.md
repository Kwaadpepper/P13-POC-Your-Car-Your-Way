# Frontend

Ce projet est un monorepo Angular (v20) orienté micro-frontends, conçu pour l'authentification et le support dans une architecture scalable.  
Chaque application est isolée, partage des styles et des composants via Native Federation.

---

## Sommaire

- [Frontend](#frontend)
  - [Sommaire](#sommaire)
  - [1. Présentation](#1-présentation)
  - [2. Prérequis](#2-prérequis)
  - [3. Architecture du projet](#3-architecture-du-projet)
  - [4. Convention de nommage](#4-convention-de-nommage)
  - [5. Gestion des styles](#5-gestion-des-styles)
  - [6. Configuration / Variables d'environnement](#6-configuration--variables-denvironnement)
  - [7. Utilisation des librairies](#7-utilisation-des-librairies)
  - [8. Bonnes pratiques Angular \& TypeScript](#8-bonnes-pratiques-angular--typescript)
    - [TypeScript](#typescript)
    - [Angular](#angular)
  - [9. Scripts d'automatisation](#9-scripts-dautomatisation)
  - [10. Démarrage local](#10-démarrage-local)
  - [11. Tests](#11-tests)
  - [12. Ajout de projet : guide contributeur](#12-ajout-de-projet--guide-contributeur)
  - [13. Déploiement \& CI/CD](#13-déploiement--cicd)
  - [14. Utiliser les images Docker du frontend](#14-utiliser-les-images-docker-du-frontend)
    - [Avec Docker](#avec-docker)
    - [Avec Podman (alternative)](#avec-podman-alternative)
    - [Accès aux services](#accès-aux-services)
    - [Commandes utiles](#commandes-utiles)
  - [15. Contribution](#15-contribution)
  - [16. Ressources complémentaires](#16-ressources-complémentaires)
  - [17. FAQ / Points d'attention](#17-faq--points-dattention)

---

## 1. Présentation

Monorepo Angular utilisant Native Federation pour isoler et partager des micro-frontends.  
Chaque projet Angular est typé strictement, utilise les composants standalone, les signals pour le state, les templates natifs, et suit les conventions de nommage.

---

## 2. Prérequis

- **Node.js** (>=18 recommandé)
- **bun** (obligatoire pour contribuer) [https://bun.sh/](https://bun.sh/)
- **Docker** ou **Podman** (pour la CI et le dev)
- Un navigateur moderne

---

## 3. Architecture du projet

- Dossier `projects/` : applications et librairies Angular (shell, MFEs, librairie partagée).
- Dossier `docker/` : fichiers Docker et configuration d'infrastructure.
- Styles partagés dans `shared/src/styles/shared.css`.
- Préfixes uniques pour chaque projet (évite collisions, facilite Native Federation).
- Organisation favorise l'intégration, l'isolation et le déploiement indépendant de chaque MFE.

---

## 4. Convention de nommage

- Préfixe unique pour chaque projet configuré dans `angular.json`.
- Tous les selectors de composants générés utilisent ce préfixe (ex : `shell-dashboard`).
- Les conventions sont vérifiées par ESLint et lors de la génération de composants.

---

## 5. Gestion des styles

- **Tailwind CSS v3** est utilisé pour la gestion des styles.
- Les composants Angular doivent importer les styles partagés en début de fichier css.
- Utilisez `@apply` pour intégrer les classes utilitaires Tailwind dans votre CSS local.

Exemple :
```css
@import "../../../../../../styles/styles.css";
.item { @apply flex items-center gap-2 w-full; }
.title { @apply font-semibold; }
.updated { @apply text-gray-500 text-sm; }
.badge { @apply min-w-[1.5rem] rounded-full text-center; }
```

---

## 6. Configuration / Variables d'environnement

- Les variables d'environnement sont gérées via `environment.*.ts` dans chaque projet.
- Utilisez `fileReplacements` dans `angular.json` pour les configurations (prod/dev).
- Les valeurs d'environnement sont publiques (jamais de secrets).

---

## 7. Utilisation des librairies

- **PrimeNG** est la bibliothèque de composants principale.
- Importez les modules nécessaires dans chaque projet.
- Consultez la [documentation officielle PrimeNG](https://primeng.org/).

---

## 8. Bonnes pratiques Angular & TypeScript

### TypeScript
- Strict typing partout
- Préférer l'inférence quand le type est évident
- Éviter `any` : utiliser `unknown` si nécessaire

### Angular
- Utiliser des **standalone components** (pas de NgModules)
- Utiliser des **signals** pour le state local
- Utiliser `computed()` pour le state dérivé
- Les **composants** doivent être petits, spécialisés et à responsabilité unique
- Utiliser `input()` et `output()` au lieu des décorateurs
- Préférer les **Reactive Forms**
- Utiliser uniquement les **bindings natifs** (`class`, `style`) : ne pas utiliser `ngClass` ou `ngStyle`
- Utiliser le contrôle de flux natif (`@if`, `@for`, `@switch`)
- Utiliser `NgOptimizedImage` pour les images statiques
- Mettre `changeDetection: ChangeDetectionStrategy.OnPush` dans le décorateur du composant
- Injecter les services avec `inject()` uniquement
- Les services singleton : `providedIn: 'root'`
- Les bindings d'hôte dans `host` de `@Component`/`@Directive`, pas de décorateurs
- Garder les templates simples, sans logique complexe
- Utiliser le pipe `async` pour gérer les observables

---

## 9. Scripts d'automatisation

- Les scripts sont dans le dossier `scripts/` et dans le `package.json`.
- **check:alias** : vérifie la validité des alias TypeScript
  ```bash
  bun run check:alias
  ```
- **mock:ws** : lance un serveur WebSocket mock
  ```bash
  bun run mock:ws
  ```
- Pour la liste complète des scripts (`build`, `start`, `lint`, etc.), voir le `package.json`.

---

## 10. Démarrage local

1. Compiler la librairie partagée :
   ```bash
   bun run build shared
   ```
2. Lancer le shell et chaque micro-frontend dans des terminaux séparés :
   ```bash
   bun run start shell
   bun run start app-reservation
   bun run start app-support
   # ... ajoute chaque MFE existant
   ```
- Le build de `shared` est nécessaire à chaque modification.
- Tous les MFEs et le shell doivent être lancés pour que l'application fonctionne correctement.

---

## 11. Tests

- Compilez d'abord la librairie partagée.
- Lancez le shell et toutes les MFEs.
- Vérifiez l'accès à [http://localhost:4200/](http://localhost:4200/)
- Pour les tests unitaires :
  ```bash
  bun run test app-reservation
  # ou
  bun run test shell
  ```
- Si vous ne souhaitez pas utiliser bun, vous pouvez lancer les tests avec npm :
  ```bash
  npm run test app-reservation
  ```

---

## 12. Ajout de projet : guide contributeur

Pour ajouter une application ou une librairie Angular :
- Utiliser la CLI Angular via bun pour générer le projet dans `projects/`
- Configurer le préfixe du projet
- Ajouter ESLint
- Définir les alias TypeScript et importer les styles partagés
- Installer PrimeNG si besoin
- Configurer Native Federation et les environnements
- Vérifier le lint et la build

➡️ **Consultez le guide détaillé** :  
[GUIDE-ajouter-un-nouveau-projet-angular-monorepo.md](./GUIDE-ajouter-un-nouveau-projet-angular-monorepo.md)

---

## 13. Déploiement & CI/CD

- Les artefacts de build sont dans `dist/`.
- Compilation en production :
  ```bash
  bun run build --project=shell
  ```
- Les workflows CI/CD automatisent le lint, le build, la publication des images Docker sur `ghcr.io`, et l'upload des artefacts.
- Les images Docker sont taguées automatiquement selon la branche ou le tag du commit.
- Le pipeline peut être déclenché manuellement via GitHub Actions.

👉 [Voir tous les workflows sur GitHub](https://github.com/Kwaadpepper/P13-POC-Your-Car-Your-Way/tree/main/.github/workflows)

---

## 14. Utiliser les images Docker du frontend

- Les fichiers Docker sont organisés dans le dossier `docker/` pour une meilleure structuration.
- Un fichier `docker-compose.yml` permet le lancement local des MFEs et des mocks.

### Avec Docker

Démarrage :
```bash
cd docker
docker-compose up --build
```

Ou depuis la racine :
```bash
docker-compose -f docker/docker-compose.yml up --build
```

### Avec Podman (alternative)

Démarrage :
```bash
cd docker
podman-compose up --build
```

Ou depuis la racine :
```bash
podman-compose -f docker/docker-compose.yml up --build
```

### Accès aux services

- Shell : [http://localhost:4200](http://localhost:4200)
- Backoffice : [http://localhost:4201](http://localhost:4201)
- Réservation : [http://localhost:4202](http://localhost:4202)
- Support : [http://localhost:4203](http://localhost:4203)
- Mock HTTP : [http://localhost:3001](http://localhost:3001)
- Mock WebSocket : [ws://localhost:3002](ws://localhost:3002)

### Commandes utiles

```bash
# Build d'un service spécifique
docker-compose -f docker/docker-compose.yml build shell
podman-compose -f docker/docker-compose.yml build shell

# Lancement en arrière-plan
docker-compose -f docker/docker-compose.yml up -d
podman-compose -f docker/docker-compose.yml up -d

# Arrêt des services
docker-compose -f docker/docker-compose.yml down
podman-compose -f docker/docker-compose.yml down
```

**Notes importantes :**
- Les services frontend dépendent des mocks pour fonctionner en local.
- Personnalisation facile : adaptez les ports ou les images dans `docker/docker-compose.yml`.
- La structure `docker/` sépare clairement l'infrastructure du code applicatif.

---

## 15. Contribution

- Forkez le dépôt et créez une branche dédiée (`feature/<votre-nom>/<description>`).
- Utilisez **bun** pour toutes les commandes de build, test, lint et scripts.
- Respectez la convention de nommage et les bonnes pratiques Angular/TypeScript.
- Rédigez des messages de commit au format Conventional Commits :  
  `feat(frontend-shell): ajoute le menu latéral`
- Vérifiez le lint et les tests avant toute PR.

---

## 16. Ressources complémentaires

- [Documentation Angular CLI](https://angular.dev/tools/cli)
- [Documentation PrimeNG](https://primeng.org/)
- [Documentation officielle Tailwind CSS](https://tailwindcss.com/)
- [Docker Compose](https://docs.docker.com/compose/)
- [Podman Compose](https://github.com/containers/podman-compose)

---

## 17. FAQ / Points d'attention

**Pourquoi standalone ?**  
Composants isolés, réutilisables et dynamiquement chargeables.

**Pourquoi signals ?**  
State local plus simple, performant et réactif.

**Pourquoi éviter `ngClass` et `ngStyle` ?**  
Les bindings natifs sont plus performants et plus lisibles.

**Pourquoi `inject()` ?**  
Meilleure composition et testabilité des services.

**Pourquoi Native Federation ?**  
Partage de composants et styles entre MFEs, tout en gardant isolation et scalabilité.

**Pourquoi organiser Docker dans un dossier séparé ?**  
Séparation des préoccupations, meilleure lisibilité du projet et réutilisabilité des configurations Docker.

**Docker vs Podman ?**  
Podman est une alternative sans daemon à Docker, plus sécurisée. Les commandes sont équivalentes avec `podman-compose`.
