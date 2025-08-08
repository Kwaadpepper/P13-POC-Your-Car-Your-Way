const { shareAll } = require('@angular-architects/native-federation/config');

module.exports = {
  shared: {
    '@angular/core': { singleton: true, strictVersion: true, requiredVersion: 'auto' },
    '@angular/common': { singleton: true, strictVersion: true, requiredVersion: 'auto' },
    '@angular/router': { singleton: true, strictVersion: true, requiredVersion: 'auto' },
    '@angular/forms': { singleton: true, strictVersion: true, requiredVersion: 'auto' },
    'primeng': { singleton: true, strictVersion: true, requiredVersion: 'auto' },
    'primeicons': { singleton: true, strictVersion: true, requiredVersion: 'auto' },
    ...shareAll({
      singleton: true,
      strictVersion: true,
      requiredVersion: 'auto'
    }),
  },
  skip: [
    /^@primeuix\//,
    /^@primeng\//,
    'rxjs/ajax',
    'rxjs/fetch',
    'rxjs/testing',
    'rxjs/webSocket',
    // Add further packages you don't need at runtime
  ]
};
