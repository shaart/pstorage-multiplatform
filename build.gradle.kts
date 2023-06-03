import org.jetbrains.compose.desktop.application.dsl.TargetFormat

val coroutinesVersion = "1.6.4"
val ktorVersion = "2.2.4"
val dateTimeVersion = "0.4.0"

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    id("app.cash.sqldelight")
    kotlin("plugin.serialization")
    id("org.flywaydb.flyway") version "9.19.1"
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
                implementation("org.xerial:sqlite-jdbc:3.34.0")
                implementation("org.flywaydb:flyway-core:9.19.1")
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
            // generate migration SQL files on build step
            migrationOutputDirectory.set(file("$buildDir/generated/db/migrations"))
        }
    }
}
tasks.register<Copy>("copyGeneratedMigrations") {
    val from = file("$buildDir/generated/db/migrations")
    val into = file("$buildDir/processedResources/jvm/main/db/migrations")
    from(from)
    rename("(.+).sql", "V$1__migration.sql") // 1.sql -> V1__migration.sql
    into(into)
    dependsOn("generateCommonMainPstorageDatabaseMigrations")
}
tasks.withType<ProcessResources> {
    dependsOn("copyGeneratedMigrations")
}


flyway {
    locations = arrayOf("filesystem:$buildDir/generated/db/migrations")
}