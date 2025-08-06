# Guide : Ajouter un nouveau projet à votre monorepo Angular

Ce guide détaille toutes les étapes pour intégrer proprement un nouveau projet (application ou librairie) dans votre workspace Angular, en respectant la structure, les conventions et l’intégration dans les scripts d’automatisation.

---

## 1. Générer le projet Angular

**Application** :
```bash
ng generate application <nom-du-projet>
```

**Librairie** :
```bash
ng generate library <nom-de-la-lib>
```

Le projet est créé dans le dossier `projects/`.

---

## 2. Ajouter ESLint au projet

```bash
ng g angular-eslint:add-eslint-to-project <nom-du-projet>
```
- Un fichier `eslint.config.js` (ESM) est créé dans le dossier du projet.
- Une cible `lint` est ajoutée dans le bloc `architect` du projet dans `angular.json`.

> **Remarque** :  
> Depuis Angular 17+, ESLint utilise un fichier de configuration ESM (`eslint.config.js` avec `export default`).  
> Si vous souhaitez supprimer le warning Node (MODULE_TYPELESS_PACKAGE_JSON) sans ajouter `"type": "module"` à la racine, vous pouvez renommer ce fichier en `eslint.config.mjs`.

---

## 3. Générer un composant avec préfixe

**Générer un composant dans votre projet :**
```bash
ng generate component <nom-du-composant> --project=<nom-du-projet>
```

Le préfixe configuré dans `angular.json` pour le projet sera automatiquement appliqué au selector du composant généré.

**Exemple :**
Si le préfixe du projet `shell` est `shell` et vous exécutez :
```bash
ng generate component dashboard --project=shell
```
Le selector du composant sera automatiquement :
```typescript
@Component({
  selector: 'shell-dashboard',
  // ...
})
```

> **Astuce :**  
> Pour garantir l’unicité des selectors et éviter les collisions (notamment avec Native Federation), chaque projet doit avoir son propre préfixe, défini dans la configuration du projet dans `angular.json`.  
> ESLint vérifie aussi ce préfixe grâce à la règle `@angular-eslint/component-selector`.

---

## 4. Définir les alias TypeScript

- Ouvrez le fichier racine `tsconfig.json`.
- Ajoutez ou modifiez les alias nécessaires dans la section `compilerOptions.paths`.
  - Exemple :
    ```json
    "@mon-alias/*": ["./projects/<nom-du-projet>/src/app/mon-dossier/*"]
    ```
- Ouvrez `projects/<nom-du-projet>/tsconfig.app.json` et ajoutez les alias spécifiques dans `compilerOptions.paths`.

**Règles à respecter :**
- Les alias globaux (qui ne pointent pas vers `./projects/*`) doivent être présents dans tous les projets.
- Les alias spécifiques à un projet (pointant vers `./projects/<nom-du-projet>`) doivent être présents uniquement dans le projet concerné.

---

## 5. Importer les styles partagés

Dans le fichier de styles principal du projet (généralement `projects/<nom-du-projet>/src/styles.css` ou `styles.scss`), ajoutez en haut du fichier :

```css
@import "./../../../shared/src/styles/shared.css";
```

Cela permet de mutualiser les styles entre les différents projets du monorepo.

---

## 6. Installer et utiliser PrimeNG

PrimeNG est la bibliothèque de composants utilisée dans le monorepo.

**Installation dans le projet :**
```bash
ng add primeng
```
ou
```bash
npm install primeng primeicons
```

**Utilisation :**
- Importez les modules PrimeNG nécessaires dans le ou les modules Angular de votre projet.
- Consultez la documentation officielle pour les composants et les thèmes : [PrimeNG Documentation](https://primeng.org/).

---

## 7. Vérifier les alias

Lancez le script de vérification :
```bash
npm run check:alias
```
Corrigez toutes les erreurs signalées par le script jusqu’à obtention d’un message de succès.

---

## 8. Lancer le lint sur le projet

```bash
ng lint <nom-du-projet>
```

Vous pouvez également lancer le lint global :
```bash
npm run lint
```

---

## 9. Vérifier la build

Testez la build du projet pour vous assurer que tout fonctionne :
```bash
ng build <nom-du-projet>
```
ou lancez la build globale :
```bash
npm run build
```

---

## 10. Adapter la configuration spécifique (Native Federation, styles, etc.)

- Si le projet doit utiliser Native Federation, configurez le builder dans `angular.json` selon les exemples présents.
- Ajoutez les dépendances ou configurations spécifiques (stylelint, tailwind, etc.) si besoin.

> **Note Native Federation** :  
> À ce jour, le fichier de configuration `federation.config.js` doit rester en CommonJS (pas de support ESM natif).  
> Si votre workspace est en `"type": "module"`, voir les workarounds dans le README ou la documentation du plugin.

---

## 11. Commit & Push

N’oubliez pas d’ajouter et de committer tous les nouveaux fichiers :
```bash
git add .
git commit -m "feat: ajout du projet <nom-du-projet>"
git push
```

---

## Checklist rapide

- [ ] Projet généré dans `projects/`
- [ ] ESLint installé et configuré (ESM ou `.mjs` si souhaité)
- [ ] Composants générés avec un préfixe unique
- [ ] Alias TypeScript définis et valides
- [ ] Script `check:alias` OK
- [ ] Styles partagés importés
- [ ] PrimeNG installé et utilisé pour les composants
- [ ] Lint OK
- [ ] Build OK
- [ ] Config spécifique adaptée (Native Federation, styles…)
- [ ] Commit effectué
