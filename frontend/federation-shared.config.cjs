const { share } = require('@angular-architects/native-federation/config');

module.exports = {
  shared: {
    ...share({
      '@angular/core': { singleton: true },
      '@angular/core/primitives/signals': { singleton: true },
      '@angular/core/primitives/di': { singleton: true },
      '@angular/common': { singleton: true },
      '@angular/common/http': { singleton: true },
      '@angular/platform-browser': { singleton: true },
      '@angular/platform-browser/animations/async': { singleton: true },
      '@angular/router': { singleton: true },
      '@angular/forms': { singleton: true },
      '@angular/cdk': { singleton: false },
      "primeng": { singleton: true },
      "primeicons": { singleton: true },
      'rxjs': { singleton: true  },
      'rxjs/operators': { singleton: true  },
      "@primeuix/styles": { singleton: true },
      "@primeuix/styled": { singleton: true },
      "@primeuix/themes": { singleton: true },
      "@primeuix/utils": { singleton: true },
    }),
  },
  skip: [
    'rxjs/ajax',
    'rxjs/fetch',
    'rxjs/testing',
    'rxjs/webSocket',
    // Add further packages you don't need at runtime
  ]
};
