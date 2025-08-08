const { withNativeFederation } = require('@angular-architects/native-federation/config');
const federationShared = require('../../shared/federation-shared.config.cjs');

module.exports = withNativeFederation({

  shared: {
    ...federationShared.shared,
  },

  skip: [
    ...federationShared.skip
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
