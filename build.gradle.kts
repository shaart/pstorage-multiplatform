import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

group = "com.github.shaart"
version = "0.1.0-SNAPSHOT"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

kotlin {
    jvm {
        jvmToolchain(17)
        withJava()
    }
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
            }
        }
        val jvmTest by getting
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "pstorage-multiplatform"
            packageVersion = "1.0.0"
            val iconsDirPath = "src/jvmMain/resources/assets/icons/taskbar"
            macOS {
                iconFile.set(project.file("$iconsDirPath/icon64.icns"))
            }
            linux {
                iconFile.set(project.file("$iconsDirPath/icon64.png"))
            }
            windows {
                iconFile.set(project.file("$iconsDirPath/icon64.ico"))
            }
        }
    }
}
