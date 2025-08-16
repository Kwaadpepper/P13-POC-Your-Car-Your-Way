# Guide : Ajouter un nouveau projet à votre monorepo Angular

Ce guide détaille les étapes pour intégrer proprement un nouveau projet (application ou librairie) dans votre workspace Angular, selon les conventions du monorepo et les meilleures pratiques Angular/TypeScript.

---

## 0. Prérequis repo (post-clone)

Après un clone, activez Husky/commitlint côté frontend :
```bash
cd frontend
npm install
npm run prepare
```
Vérifiez les hooks git :
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

## 2. Configurer le préfixe du projet

Ouvrez `angular.json` et vérifiez que le préfixe du projet est unique (utile pour Native Federation, vérifié par ESLint).

---

## 3. Ajouter ESLint au projet

```bash
ng g angular-eslint:add-eslint-to-project <nom-du-projet>
```
Si besoin, renommez le fichier de config en `.mjs` pour éviter le warning Node.

---

## 4. Générer les composants avec préfixe

```bash
ng generate component <nom-du-composant> --project=<nom-du-projet>
```
Astuce : le préfixe est appliqué automatiquement au selector.

---

## 5. Définir les alias TypeScript

Dans le `tsconfig.json` racine et celui du projet, ajoutez les alias nécessaires :
```json
"@ycyw/mon-alias/*": ["./projects/<nom-du-projet>/src/app/mon-dossier/*"]
```
Respectez la séparation entre alias globaux et spécifiques.

---

## 6. Importer les styles partagés

Dans le fichier de styles du projet (`projects/<nom-du-projet>/src/styles.css`), ajoutez en haut :
```css
@import "@ycyw/styles";
```
Respectez l’ordre d’import attendu par Tailwind.

---

## 7. Installer et utiliser PrimeNG

```bash
ng add primeng
# ou
npm install primeng primeicons
```
Importez les modules nécessaires dans votre projet.

---

## 8. Configurer Native Federation

- Utilisez la config partagée : `frontend/shared/federation-shared.config.cjs`
- Dans chaque projet, créez : `projects/<nom-du-projet>/federation.config.js`
- Gardez la syntaxe CommonJS (`require`/`module.exports`), pas de `"type": "module"` dans le frontend.

---

## 9. Configurer les environnements Angular

Générez les fichiers d’environnement :
```bash
ng generate environments --project=<nom-du-projet>
```
Ajoutez la configuration de `fileReplacements` dans `angular.json` pour le dev/prod.

---

## 10. Vérifier les alias

Lancez :
```bash
bun run check:alias
```
Corrigez les erreurs jusqu’à obtention d’un message de succès.

---

## 11. Lancer le lint et la build

```bash
ng lint <nom-du-projet>
ng build <nom-du-projet>
```

---

## 12. Conventional Commits (scopes)

Ajoutez/maintenez les scopes dans `.vscode/settings.json` pour l’extension Conventional Commits.

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
- [ ] Préfixe unique configuré
- [ ] ESLint installé et configuré
- [ ] Composants générés avec préfixe
- [ ] Alias TypeScript définis et valides
- [ ] Styles partagés importés
- [ ] PrimeNG installé et utilisé
- [ ] Native Federation branchée sur la config partagée
- [ ] Fichiers d’environnement créés et fileReplacements configurés
- [ ] Lint OK
- [ ] Build OK
- [ ] Scopes Conventional Commits mis à jour dans `.vscode/settings.json`
- [ ] Commit effectué

---
