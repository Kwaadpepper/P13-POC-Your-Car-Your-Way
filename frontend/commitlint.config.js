const fs = require('node:fs')
const path = require('node:path')
const JSON5 = require('json5')

function getScopesFromVSCodeSettings() {
  try {
    const settingsPath = path.resolve(process.cwd(), '.vscode/settings.json')
    const raw = fs.readFileSync(settingsPath, 'utf8')
    const settings = JSON5.parse(raw)
    const scopes = settings?.['conventionalCommits.scopes']
    return Array.isArray(scopes) ? scopes : []
  } catch {
    return []
  }
}

const dynamicScopes = getScopesFromVSCodeSettings()

module.exports = {
  extends: ['@commitlint/config-conventional'],
  rules: {
    // Active la restriction des scopes uniquement si la liste existe
    ...(dynamicScopes.length
      ? { 'scope-enum': [2, 'always', dynamicScopes] }
      : {}),
    'scope-empty': [2, 'never'],
  },
}
