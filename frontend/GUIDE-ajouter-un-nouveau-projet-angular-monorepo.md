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

## 3. Définir les alias TypeScript

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

## 4. Vérifier les alias

Lancez le script de vérification :
```bash
npm run check:alias
```
Corrigez toutes les erreurs signalées par le script jusqu’à obtention d’un message de succès.

---

## 5. Lancer le lint sur le projet

```bash
ng lint <nom-du-projet>
```

Vous pouvez également lancer le lint global :
```bash
npm run lint
```

---

## 6. Vérifier la build

Testez la build du projet pour vous assurer que tout fonctionne :
```bash
ng build <nom-du-projet>
```
ou lancez la build globale :
```bash
npm run build
```

---

## 7. Adapter la configuration spécifique (Native Federation, styles, etc.)

- Si le projet doit utiliser Native Federation, configurez le builder dans `angular.json` selon les exemples présents.
- Ajoutez les dépendances ou configurations spécifiques (stylelint, tailwind, etc.) si besoin.

> **Note Native Federation** :  
> À ce jour, le fichier de configuration `federation.config.js` doit rester en CommonJS (pas de support ESM natif).  
> Si votre workspace est en `"type": "module"`, voir les workarounds dans le README ou la documentation du plugin.

---

## 8. Commit & Push

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
- [ ] Alias TypeScript définis et valides
- [ ] Script `check:alias` OK
- [ ] Lint OK
- [ ] Build OK
- [ ] Config spécifique adaptée (Native Federation, styles…)
- [ ] Commit effectué

---

> Dupliquez ce guide dans le README ou le wiki du dépôt pour faciliter l’onboarding des nouveaux contributeurs.
