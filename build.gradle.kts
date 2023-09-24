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
    id("com.gorylenko.gradle-git-properties") version "2.4.1"
}

group = "com.github.shaart"
version = extra["app.version"].toString()
val applicationName = "pstorage-multiplatform"
val fullAppPackage = "com.github.shaart.pstorage.multiplatform"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

val sqldelightVersion = project.findProperty("sqldelight.version")
val composeVersion = project.findProperty("compose.version")

kotlin {
    jvm {
        jvmToolchain(17)
        withJava()
    }
    sourceSets {
        val jvmMain by getting {
            dependencies {
                // ui
                implementation("org.jetbrains.compose.material:material-icons-extended-desktop:$composeVersion")
                implementation(compose.desktop.currentOs)

                // kotlin
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")

                // db
                implementation("app.cash.sqldelight:sqlite-driver:$sqldelightVersion")
                implementation("org.xerial:sqlite-jdbc:3.42.0.0")
                implementation("org.flywaydb:flyway-core:9.19.1")

                // logging
                implementation("org.slf4j:slf4j-api:2.0.7")
                implementation("ch.qos.logback:logback-classic:1.4.7")
                implementation("ch.qos.logback:logback-core:1.4.7")

                // crypto
                implementation("org.springframework.security:spring-security-crypto:6.1.1")
                // for org.springframework.security:spring-security-crypto
                implementation("commons-logging:commons-logging:1.2")
            }
        }
        val jvmTest by getting
        val commonTest by getting {
            dependencies {
                implementation("org.junit.jupiter:junit-jupiter-api:5.9.3")
                runtimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.3")
                implementation("org.assertj:assertj-core:3.6.1")
            }
        }
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

compose.desktop {
    application {
        buildTypes.release.proguard {
            isEnabled.set(false)
            configurationFiles.from(project.file("proguard-rules.pro"))
        }

        mainClass =
            if (System.getProperty("app.preview", "false") == "true")
                "com.github.shaart.pstorage.multiplatform.PreviewMainKt"
            else
                "com.github.shaart.pstorage.multiplatform.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)

            // For release build. List can be checked by "suggestRuntimeModules" task
            modules(
                "java.compiler", "java.instrument", "java.sql", "jdk.unsupported",
                "java.naming",
            )

            packageName = applicationName
            packageVersion = project.version.toString()
            licenseFile.set(project.file("LICENSE"))

            val iconsDirPath = "src/jvmMain/resources/assets/icons/taskbar"
            val iconsDir = project.file(iconsDirPath)
            macOS {
                iconFile.set(iconsDir.resolve("icon64.icns"))
                bundleID = fullAppPackage
                appCategory = "public.app-category.utilities"
                signing {
                    sign.set(false)
                }
            }
            linux {
                iconFile.set(iconsDir.resolve("icon64.png"))
            }
            windows {
                iconFile.set(iconsDir.resolve("icon64.ico"))
                console = false
                perUserInstall = true
                dirChooser = true
                upgradeUuid = "83D7C93F-CD22-4CC8-B259-4B95C4AC063F"
                menuGroup = applicationName
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
tasks.register<Copy>("processGeneratedMigrations") {
    group = "prebuild"
    val from = file("$buildDir/generated/db/migrations")
    val into = file("$buildDir/processedResources/jvm/main/db/migrations")
    from(from)
    rename("(.+).sql", "V$1__migration.sql") // 1.sql -> V1__migration.sql
    into(into)
    dependsOn("generateCommonMainPstorageDatabaseMigrations")
}
tasks.named("processResources") {
    dependsOn("processGeneratedMigrations")
    dependsOn("generateGitProperties")
}
tasks.named("jvmProcessResources") {
    dependsOn("processGeneratedMigrations")
    dependsOn("generateGitProperties")
}
tasks.withType<Jar> {
    manifest {
        attributes(
            mutableMapOf(Pair("Class-Path","."))
        )
    }
}

flyway {
    locations = arrayOf("filesystem:$buildDir/generated/db/migrations")
}

gitProperties {
    gitPropertiesName = "git.properties"
    keys = listOf(
        "git.branch", "git.build.version",
        "git.commit.id", "git.commit.id.abbrev",
        "git.commit.time", "git.closest.tag.name",
    )
    gitPropertiesResourceDir.set(file("$buildDir/processedResources/jvm/main"))
    dateFormatTimeZone = "UTC"
}