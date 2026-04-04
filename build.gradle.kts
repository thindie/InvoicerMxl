import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin("jvm") version "1.9.24"
  id("org.jetbrains.compose") version "1.6.11"
}

group = "com.thindie"
// Keep in sync with com.thindie.invoicer.application.update.AppVersion.SEMVER
version = "2.0.6"

repositories {
  google()
  mavenCentral()
  maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

val ktorVersion = "2.3.11"

dependencies {
  implementation(compose.desktop.currentOs)
  @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
  implementation(compose.material3)
  implementation("io.ktor:ktor-client-core:${ktorVersion}")
  implementation("io.ktor:ktor-client-auth:${ktorVersion}")
  implementation("io.ktor:ktor-client-cio:${ktorVersion}")

  testImplementation(kotlin("test"))
  testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
  testImplementation("io.mockk:mockk:1.13.11")
  testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.1")
}

tasks.test {
  useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
  kotlinOptions.jvmTarget = "17"
}

compose.desktop {
  application {
	buildTypes.release.proguard {
	  isEnabled.set(false)
	}
	mainClass = "com.thindie.invoicer.ApplicationKt"
	nativeDistributions {
	  vendor = "thindie"
	  copyright = "2026 thindie"
	  targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
	  packageName = "InvoicerMxl"
	  packageVersion = "2.0.6"
	  appResourcesRootDir.set(project.layout.projectDirectory.dir("src/appdata"))
	  buildTypes.release.proguard {
		configurationFiles.from(project.file("compose-desktop.pro"))
	  }
	}
  }
}