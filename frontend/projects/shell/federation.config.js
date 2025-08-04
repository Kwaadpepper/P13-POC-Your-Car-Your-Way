const { shareAll, withNativeFederation } = require('@angular-architects/native-federation/config');

module.exports = withNativeFederation({
  name: 'shell',
  remotes: {
    'users-app': 'http://localhost:4201/remoteEntry.json',
  },
  shared: {
    ...shareAll({ singleton: true, strictVersion: true, requiredVersion: 'auto' }),
    'primeng': { singleton: true, strictVersion: true, requiredVersion: 'auto' },
    '@primeng/themes': { singleton: true, strictVersion: true, requiredVersion: 'auto' },
    // '@ycyw/shared' seulement si tu utilises la lib "shared" comme package Angular
  },
  features: {
    ignoreUnusedDeps: true
  }
});
