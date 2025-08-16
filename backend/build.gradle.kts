import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.jvm.toolchain.JavaLanguageVersion
import net.ltgt.gradle.errorprone.errorprone

plugins {
  alias(libs.plugins.errorprone) apply false
}

val enableNullAway = providers.gradleProperty("nullaway").orNull == "true"

subprojects {
  group = "com.ycyw"

  pluginManager.apply("java")

  repositories {
    mavenCentral()
    maven("https://repo.spring.io/release")
  }

  extensions.configure(JavaPluginExtension::class.java) {
    // Désambiguïser explicitement vers Int
    toolchain.languageVersion.set(JavaLanguageVersion.of(rootProject.libs.versions.java.get().toInt()))
  }

  dependencies {
    add("compileOnly", rootProject.libs.jspecify)
  }

  if (enableNullAway) {
    pluginManager.apply("net.ltgt.errorprone")
    dependencies {
      add("errorprone", rootProject.libs.errorprone.core)
      add("errorprone", rootProject.libs.nullaway)
    }
    tasks.withType(JavaCompile::class.java).configureEach {
      options.errorprone {
        error("NullAway")
        option("NullAway:AnnotatedPackages", "com.ycyw")
        option("NullAway:TreatGeneratedAsUnannotated", "true")
      }
      options.compilerArgs.add("-Werror")
    }
  }
}
