const { withNativeFederation } = require('@angular-architects/native-federation/config');
const federationShared = require('../../federation-shared.config.cjs');

module.exports = withNativeFederation({

  name: 'app-reservation',

  exposes: {
    "./routes": "./projects/app-reservation/src/app/app.routes.ts",
  },

  shared: {
    ...federationShared.shared,
       // ...shareAll({
    //   singleton: true,
    //   strictVersion: true,
    //   requiredVersion: 'auto'
    // }),
  },

  skip: [
    ...federationShared.skip,
  ],

  // Please read our FAQ about sharing libs:
  // https://shorturl.at/jmzH0

  features: {
    // New feature for more performance and avoiding
    // issues with node libs. Comment this out to
    // get the traditional behavior:
    ignoreUnusedDeps: true
  }

});
