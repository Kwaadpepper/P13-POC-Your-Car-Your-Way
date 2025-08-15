const { share } = require('@angular-architects/native-federation/config');

module.exports = {
  shared: {
    ...share({
      '@angular/core': { singleton: true, includeSecondaries: true },
      '@angular/common': { singleton: true, includeSecondaries: true },
      '@angular/platform-browser': { singleton: true },
      '@angular/router': { singleton: true },
      '@angular/common/http': { singleton: true },
      '@angular/forms': { singleton: true },
      '@angular/cdk': { singleton: false },
      "primeng": { singleton: true },
      "primeng/icons": { singleton: true },
      "primeicons": { singleton: true },
      'rxjs': { singleton: true  },
      'rxjs/operators': { singleton: true  }
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
