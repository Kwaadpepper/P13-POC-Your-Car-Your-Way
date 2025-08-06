import fs from 'fs';
import JSON5 from 'json5';
import path from 'path';
import process from 'process';

const utils = {
  readJson(filePath) {
    if (!fs.existsSync(filePath)) return null;
    try {
      const content = fs.readFileSync(filePath, 'utf-8');
      return JSON5.parse(content);
    } catch {
      return null;
    }
  },

  getAliasGroups(rootAliases) {
    const globalAliases = [];
    const projectAliasesMap = {};
    Object.entries(rootAliases).forEach(([alias, targets]) => {
      const target = targets[0];
      if (!target.startsWith('./projects/')) {
        globalAliases.push(alias);
      } else {
        const match = target.match(/^\.\/projects\/([^/]+)\//);
        if (match) {
          const project = match[1];
          if (!projectAliasesMap[project]) projectAliasesMap[project] = [];
          projectAliasesMap[project].push(alias);
        }
      }
    });
    return { globalAliases, projectAliasesMap };
  },

  findTsconfigApps(projectsDir) {
    let results = [];
    const entries = fs.readdirSync(projectsDir, { withFileTypes: true });
    for (const entry of entries) {
      const fullPath = path.join(projectsDir, entry.name);
      if (entry.isDirectory()) {
        results = results.concat(utils.findTsconfigApps(fullPath));
      } else if (entry.name === 'tsconfig.app.json') {
        results.push(fullPath);
      }
    }
    return results;
  },

  getProjectName(appConfigPath) {
    const match = appConfigPath.match(/\/projects\/([^/]+)\//);
    return match ? match[1] : null;
  }
};

// ESM: __dirname is not defined, so use import.meta.url
const __dirname = path.dirname(new URL(import.meta.url).pathname);

const rootTsconfigPath = path.resolve(__dirname, '../tsconfig.json');
const rootTsconfig = utils.readJson(rootTsconfigPath);

if (!rootTsconfig) {
  console.error('Erreur: tsconfig.json racine introuvable ou mal formé.');
  process.exit(1);
}

const rootAliases = rootTsconfig.compilerOptions?.paths ?? {};
const { globalAliases, projectAliasesMap } = utils.getAliasGroups(rootAliases);
const tsconfigAppFiles = utils.findTsconfigApps(path.resolve(__dirname, '../projects'));

let errorFound = false;

tsconfigAppFiles.forEach(appConfigPath => {
  const appConfig = utils.readJson(appConfigPath);
  if (!appConfig) return;
  const appAliases = appConfig.compilerOptions?.paths ?? {};
  const projectName = utils.getProjectName(appConfigPath);

  // Vérifie les alias globaux
  globalAliases.forEach(alias => {
    if (!(alias in appAliases)) {
      console.error(
        `Erreur: alias global "${alias}" manquant dans ${appConfigPath}`
      );
      errorFound = true;
    }
  });

  // Vérifie les alias projets spécifiques
  if (projectName && projectAliasesMap[projectName]) {
    projectAliasesMap[projectName].forEach(alias => {
      if (!(alias in appAliases)) {
        console.error(
          `Erreur: alias projet "${alias}" manquant dans ${appConfigPath} (projet "${projectName}")`
        );
        errorFound = true;
      }
    });
  }
});

if (errorFound) {
  process.exit(1);
}
