const fs = require('node:fs');
const path = require('node:path');
const JSON5 = require('json5');

/**
 * Lit la liste des scopes depuis un settings.json donné
 */
function getScopesFromVSCodeSettings(settingsPath) {
  try {
    const raw = fs.readFileSync(settingsPath, 'utf8');
    const settings = JSON5.parse(raw);
    const scopes = settings?.['conventionalCommits.scopes'];
    return Array.isArray(scopes) ? scopes : [];
  } catch {
    return [];
  }
}

/**
 * Agrège les scopes du frontend et du backend
 */
function getAllScopes() {
  const repoRoot = path.resolve(__dirname);

  const globalSettings = path.join(repoRoot, '', '.vscode', 'settings.json');
  const frontendSettings = path.join(repoRoot, 'frontend', '.vscode', 'settings.json');
  const backendSettings = path.join(repoRoot, 'backend', '.vscode', 'settings.json');

  const globalScopes = getScopesFromVSCodeSettings(globalSettings);
  const frontendScopes = getScopesFromVSCodeSettings(frontendSettings);
  const backendScopes = getScopesFromVSCodeSettings(backendSettings);

  // Fusionne et déduplique
  return Array.from(new Set([...globalScopes, ...frontendScopes, ...backendScopes])).filter(Boolean);
}

const allScopes = getAllScopes();

module.exports = {
  extends: ['@commitlint/config-conventional'],
  rules: {
    ...(allScopes.length
      ? { 'scope-enum': [2, 'always', allScopes] }
      : {}),
    'scope-empty': [2, 'never'],
  },
};
