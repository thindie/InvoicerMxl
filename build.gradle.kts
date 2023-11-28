import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.8.0"
    kotlin("kapt") version "1.8.0"
    id("org.jetbrains.compose") version "1.3.0"
}

group = "me.alex"
version = "1.0"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

dependencies {
    val daggerVersion = "2.45"
    implementation(compose.desktop.currentOs)
    implementation("com.google.dagger:dagger:$daggerVersion")
    kapt("com.google.dagger:dagger-compiler:$daggerVersion")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}





compose.desktop {
    application {
        buildTypes.release.proguard {
            isEnabled.set(false)
        }
        mainClass = "MainKt"
        nativeDistributions {
            vendor = "thindie"
            copyright = "2023 thindie"
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "InvoicerMxl"
            packageVersion = "1.0.0"
            appResourcesRootDir.set(project.layout.projectDirectory.dir("src/appdata"))

        }
    }
}