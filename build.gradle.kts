import org.jetbrains.compose.desktop.application.dsl.TargetFormat

val coroutinesVersion = "1.6.4"
val ktorVersion = "2.2.4"
val dateTimeVersion = "0.4.0"

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    id("app.cash.sqldelight")
    kotlin("plugin.serialization")
}

group = "com.github.shaart"
version = "1.0.0"
val applicationName = "pstorage-multiplatform"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

val sqldelightVersion = project.findProperty("sqldelight.version")

kotlin {
    jvm {
        jvmToolchain(17)
        withJava()
    }
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation("app.cash.sqldelight:sqlite-driver:$sqldelightVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
                implementation("io.ktor:ktor-client-core:$ktorVersion")
                implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:$dateTimeVersion")
            }
        }
        val jvmTest by getting
    }
}

compose.desktop {
    application {
        mainClass = "com.github.shaart.pstorage.multiplatform.MainKt"
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

sqldelight {
    databases {
        create("PstorageDatabase") {
            packageName.set("com.github.shaart.pstorage.multiplatform.db")
            verifyMigrations.set(true)
            deriveSchemaFromMigrations.set(true)
        }
    }
}