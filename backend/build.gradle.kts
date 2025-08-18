import net.ltgt.gradle.errorprone.errorprone
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.jvm.toolchain.JavaLanguageVersion

plugins {
  alias(libs.plugins.spotless)
  alias(libs.plugins.checkstyle)
  alias(libs.plugins.errorprone) apply false
}

val enableNullAway = providers.gradleProperty("nullaway").orNull == "true"

// Spotless pour tout le monorepo
spotless {
  // * Java
  java {
    googleJavaFormat(rootProject.libs.versions.google.java.format.get())
    removeUnusedImports()
    importOrder()
    formatAnnotations()
    target("**/*.java")
    targetExclude("**/build/**", "**/generated/**")
  }
  // * Scripts Gradle Kotlin
  kotlinGradle {
    ktlint(rootProject.libs.versions.ktlint.get())
    target("**/*.gradle.kts")
    targetExclude("**/build/**")
  }
}

// * Intègre Spotless dans 'check'
tasks.matching { it.name == "check" }.configureEach {
  dependsOn("spotlessCheck")
}

subprojects {
  group = "com.ycyw"
  pluginManager.apply("java")

  repositories {
    mavenCentral()
    maven("https://repo.spring.io/release")
  }

  // * Toolchain Java via le catalog (référence depuis le projet racine)
  extensions.configure(JavaPluginExtension::class.java) {
    toolchain.languageVersion.set(
      JavaLanguageVersion.of(rootProject.libs.versions.java.get().toInt()),
    )
  }

  // * Dépendances communes
  dependencies {
    add("compileOnly", rootProject.libs.jspecify)
  }

  // * ErrorProne / NullAway (optionnel)
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

  // * Checkstyle
  pluginManager.apply("checkstyle")

  checkstyle {
    toolVersion = rootProject.libs.versions.checkstyle.get()
    configFile = rootProject.file("config/checkstyle/checkstyle.xml")
    isShowViolations = true
  }

  tasks.withType<Checkstyle>().configureEach {
    enabled = fileTree("src/main/java").files.isNotEmpty()
    reports {
      html.required.set(true)
      xml.required.set(false)
    }
  }

  tasks.named("check") {
    dependsOn("checkstyleMain", "checkstyleTest")
  }
}
