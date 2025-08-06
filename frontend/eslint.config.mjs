// @ts-check
import eslint from "@eslint/js";
import stylistic from "@stylistic/eslint-plugin";
import angular from "angular-eslint";
import tseslint from "typescript-eslint";

export default tseslint.config(
  // Ignorer les fichiers qui ne doivent pas être lintés
  {
    ignores: [
      "**/node_modules/**",
      "dist/**",
      "projects/**/dist/**",
      "src/polyfills.ts",
      "src/test.ts"
    ],
  },
  {
    files: ["**/*.ts"],
    extends: [
      // Règles générales d'ESLint
      eslint.configs.recommended,

      // Règles recommandées pour TypeScript et de style
      ...tseslint.configs.recommended,
      ...tseslint.configs.stylistic,

      // Règles de style @stylistic
      stylistic.configs["recommended"],

      // Règles recommandées pour Angular TypeScript
      ...angular.configs.tsRecommended,
    ],
    processor: angular.processInlineTemplates,
    rules: {
      // Assurez-vous que vos sélecteurs de directives sont cohérents
      "@typescript-eslint/no-explicit-any": "error",
      "@angular-eslint/directive-selector": [
        "error",
        {
          "type": "attribute",
          "prefix": "app",
          "style": "camelCase"
        }
      ],
      "@angular-eslint/component-selector": [
        "error",
        {
          "type": "element",
          "prefix": "app",
          "style": "kebab-case"
        }
      ],
      "@typescript-eslint/no-unused-vars": [
        "error",
        {
          "argsIgnorePattern": "^_",
          "varsIgnorePattern": "^_",
          "caughtErrorsIgnorePattern": "^_"
        }
      ],
    }
  },
  {
    files: ["**/*.html"],
    extends: [
      // Règles recommandées pour les templates HTML d'Angular
      ...angular.configs.templateRecommended,

      // Règles recommandées pour l'accessibilité dans les templates
      ...angular.configs.templateAccessibility,
    ],
    rules: {
      // Exemples de règles que vous pouvez ajouter pour le HTML
      // "@angular-eslint/template/no-negated-async": "error"
    }
  }
);
