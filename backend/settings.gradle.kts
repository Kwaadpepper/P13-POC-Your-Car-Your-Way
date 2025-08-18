pluginManagement {
  repositories {
    gradlePluginPortal()
    mavenCentral()
  }
}

dependencyResolutionManagement {
  repositories {
    mavenCentral()
    maven("https://repo.spring.io/release")
  }
  versionCatalogs {
    create("buildSrcLibs") {
      from(files("./gradle/libs.versions.toml"))
    }
  }
}

rootProject.name = "ycyw-backend"

include(
  "platform:config-server",
  "platform:service-registry",
  "platform:gateway",
  "services:user-service",
)
