const { shareAll } = require('@angular-architects/native-federation/config');

module.exports = {
  shared: {
    '@angular/core': { singleton: true, strictVersion: true, requiredVersion: 'auto' },
    '@angular/core/primitives/signals': { singleton: true, strictVersion: true, requiredVersion: 'auto' },
    '@angular/core/primitives/di': { singleton: true, strictVersion: true, requiredVersion: 'auto' },
    '@angular/common': { singleton: true, strictVersion: true, requiredVersion: 'auto' },
    '@angular/common/http': { singleton: true, strictVersion: true, requiredVersion: 'auto' },
    '@angular/platform-browser': { singleton: true, strictVersion: true, requiredVersion: 'auto' },
    '@angular/platform-browser/animations/async': { singleton: true, strictVersion: true, requiredVersion: 'auto' },
    '@angular/platform-browser-dynamic': { singleton: true, strictVersion: true, requiredVersion: 'auto' },
    '@angular/router': { singleton: true, strictVersion: true, requiredVersion: 'auto' },
    '@angular/forms': { singleton: true, strictVersion: true, requiredVersion: 'auto' },
    '@angular/cdk/drag-drop': { singleton: true, strictVersion: true, requiredVersion: 'auto' },
    'rxjs': { singleton: true, strictVersion: true, requiredVersion: 'auto' },
    'rxjs/operators': { singleton: true, strictVersion: true, requiredVersion: 'auto' },
    'primeng': { singleton: true, strictVersion: true, requiredVersion: 'auto' },
    'primeicons': { singleton: true, strictVersion: true, requiredVersion: 'auto' },
    // ...shareAll({
    //   singleton: true,
    //   strictVersion: true,
    //   requiredVersion: 'auto'
    // }),
  },
  skip: [
    'rxjs/ajax',
    'rxjs/fetch',
    'rxjs/testing',
    'rxjs/webSocket',
    // Add further packages you don't need at runtime
  ]
};
