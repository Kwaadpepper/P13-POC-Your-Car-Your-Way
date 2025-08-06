# Frontend

Ce projet est un monorepo Angular (version 20).

---

## 1. Prérequis

- **Node.js** (version recommandée : >=18)
- **bun** (optionnel mais recommandé) [https://bun.sh/](https://bun.sh/)
- Un navigateur moderne (Chrome, Firefox, Edge…)

---

## 2. Architecture du projet

Le monorepo utilise la structure standard Angular :
- Le dossier `projects/` contient les différentes applications et librairies.
- Les styles partagés sont mutualisés dans `shared/src/styles/shared.css`.
- Chaque projet possède son propre préfixe pour les composants.

---

## 3. Configuration / Variables d’environnement

**TODO**  
La gestion des variables d’environnement et la configuration spécifique seront documentées ultérieurement.

---

## 4. Gestion des styles

Dans chaque projet, le fichier de styles principal (généralement `projects/<nom-du-projet>/src/styles.css`) doit importer les styles partagés :

```css
@import "./../../../shared/src/styles/shared.css";
```

Cela permet d’uniformiser l’apparence des applications et d’éviter la duplication de styles.

---

## 5. Convention de nommage

**Prefixe des composants**  
Chaque projet Angular doit définir un préfixe unique dans son fichier `angular.json`.  
Tous les tags des composants générés dans un projet utiliseront ce préfixe (ex : `shell-dashboard`, `support-dashboard`).  
Cela évite les collisions de selectors, notamment lors de l’utilisation de Native Federation.

---

## 8. Utilisation des librairies

La bibliothèque de composants principale du monorepo est **PrimeNG**.

**Installation :**
```bash
ng add primeng
# ou
npm install primeng primeicons
# ou
bun add primeng primeicons
```

**Utilisation :**  
Importez les modules nécessaires dans vos modules Angular selon la [documentation officielle de PrimeNG](https://primeng.org/).

---

## 9. Scripts d’automatisation

**TODO**  
Les scripts personnalisés (vérification des alias, formatage, etc.) seront ajoutés prochainement.

---

## 10. Déploiement

**TODO**  
La procédure de déploiement sera documentée ultérieurement.

---

## 11. Contribution

**TODO**  
Les instructions détaillées pour contribuer au projet seront ajoutées prochainement.

---

## 🖥️ Développement local

Pour lancer le serveur de développement sur l’application principale (exemple : `shell`) :

```bash
bun run ng serve shell
# ou
ng serve shell
```

Ouvrez [http://localhost:4200/](http://localhost:4200/)  
L’application se rechargera automatiquement lors de la modification des fichiers sources.

---

## 🧹 Linter le code

Pour lancer le lint sur un projet :

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

Pour compiler un projet spécifique :

```bash
ng build <nom-du-projet>
```

Pour tous les projets :

```bash
npm run build
# ou
bun run build
```

Les artefacts de build sont générés dans le dossier `dist/`.

---

## 📚 Guide d’ajout de projet

Consultez le guide détaillé pour ajouter un nouveau projet, générer des composants, configurer les alias TypeScript, mutualiser les styles, et respecter les conventions du monorepo :

➡️ [Guide : Ajouter un nouveau projet à votre monorepo Angular](./GUIDE-ajouter-un-nouveau-projet-angular-monorepo.md)

---

## 🔗 Ressources complémentaires

- [Documentation Angular CLI](https://angular.dev/tools/cli)
- [Documentation PrimeNG](https://primeng.org/)
