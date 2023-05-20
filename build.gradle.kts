import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

group = "com.github.shaart"
version = "1.0.0"
val applicationName = "pstorage-multiplatform"

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
            packageName = applicationName
            packageVersion = project.version.toString()
            val iconsDirPath = "src/jvmMain/resources/assets/icons/taskbar"
            val iconsDir = project.file(iconsDirPath)
            macOS {
                iconFile.set(iconsDir.resolve("icon64.icns"))
            }
            linux {
                iconFile.set(iconsDir.resolve("icon64.png"))
            }
            windows {
                iconFile.set(iconsDir.resolve("icon64.ico"))
            }
        }
    }
}
