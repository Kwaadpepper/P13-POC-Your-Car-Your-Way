import org.gradle.api.DefaultTask
import org.gradle.api.tasks.SourceSetContainer
import java.io.File

/*
 Refactorisé : generate-package-info.gradle.kts

 - extrait la chaîne d'annotations en variable annotationsTemplate (en haut)
 - structure le code en plusieurs fonctions pour plus de lisibilité
 - capture `project` en configuration-time (val proj) pour éviter l'appel à Task.project à l'exécution
 - reste compatible avec les options suivantes (via -P) :
    -PgeneratePackageInfoRoots="com.ycyw,com.example"
    -PgeneratePackageInfoOverwrite=true
 Usage:
   ./gradlew :<module>:generatePackageInfoNullMarked
   ./gradlew :<module>:generatePackageInfoNullMarked -PgeneratePackageInfoRoots="com.ycyw,com.foo" -PgeneratePackageInfoOverwrite=true
*/

val proj = project

// Annotations placées ici en haut et réutilisées par la génération
val annotationsTemplate =
  """
  @org.eclipse.jdt.annotation.NonNullByDefault
  @org.jspecify.annotations.NullMarked
  """.trimIndent()

// Valeurs par défaut (modifiable via -P...)
private val defaultAnnotatedRoots = listOf("com.ycyw")

private fun configuredAnnotatedRoots(): List<String> =
  (proj.findProperty("generatePackageInfoRoots") as? String)
    ?.split(',')
    ?.map { it.trim() }
    ?.filter { it.isNotEmpty() }
    ?: defaultAnnotatedRoots

private fun configuredOverwrite(): Boolean = (proj.findProperty("generatePackageInfoOverwrite") as? String)?.toBoolean() ?: false

private fun collectJavaSourceDirs(): List<File> {
  val sourceSets = proj.extensions.findByType(SourceSetContainer::class.java) ?: return emptyList()
  return sourceSets
    .getByName("main")
    .java.srcDirs
    .filter { it.exists() }
}

private fun shouldAnnotatePackage(
  packageName: String,
  annotatedRoots: List<String>,
): Boolean = annotatedRoots.any { root -> packageName == root || packageName.startsWith("$root.") }

private fun generatePackageInfoForDir(
  dir: File,
  srcDir: File,
  annotatedRoots: List<String>,
  overwriteExisting: Boolean,
  annotations: String,
) {
  val relative = dir.relativeTo(srcDir).invariantSeparatorsPath
  if (relative.isEmpty()) return

  val packageName = relative.replace('/', '.').replace('\\', '.')
  if (!shouldAnnotatePackage(packageName, annotatedRoots)) return

  val pkgFile = File(dir, "package-info.java")
  if (pkgFile.exists() && !overwriteExisting) {
    // already present and not allowed to overwrite
    return
  }

  val content =
    """
    $annotations
    package $packageName;
    """.trimIndent()

  pkgFile.parentFile?.mkdirs()
  pkgFile.writeText(content)
  println("Generated: ${pkgFile.path}")
}

pluginManager.withPlugin("java") {
  val generateTask =
    tasks.register("generatePackageInfoNullMarked", DefaultTask::class.java) {
      group = "generation"
      description = "Génère package-info.java (@NullMarked + @NonNullByDefault) pour les packages sous ${configuredAnnotatedRoots()}"

      doLast {
        val annotatedRoots = configuredAnnotatedRoots()
        val overwriteExisting = configuredOverwrite()
        val javaSrcDirs = collectJavaSourceDirs()

        if (javaSrcDirs.isEmpty()) {
          println("No java source directories for project ${proj.path}, skipping")
          return@doLast
        }

        javaSrcDirs.forEach { srcDir ->
          srcDir
            .walkTopDown()
            .filter { it.isDirectory && it != srcDir } // ignore root srcDir itself
            .forEach { dir ->
              generatePackageInfoForDir(dir, srcDir, annotatedRoots, overwriteExisting, annotationsTemplate)
            }
        }
      }
    }

  // Make sure compileJava depends on this generation task (configured at configuration time)
  tasks.named("compileJava").configure {
    dependsOn(generateTask)
  }
}
