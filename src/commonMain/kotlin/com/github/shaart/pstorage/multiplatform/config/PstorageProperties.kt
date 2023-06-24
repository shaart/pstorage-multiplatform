package com.github.shaart.pstorage.multiplatform.config

import java.util.*

data class PstorageProperties(
    var applicationName: String = "pstorage",
    var version: Version = Version(),
    var validation: Validation = Validation(),
    var database: Database = Database(),
    var flyway: FlywayProperties = FlywayProperties(),
    var ui: UiProperties = UiProperties(),
) {
    fun populate(gitProperties: Properties) {
        version.git = Git(
            branch = gitProperties["git.branch"].toString(),
            buildVersion = gitProperties["git.build.version"].toString(),
            tag = gitProperties["git.closest.tag.name"].toString(),
            commitId = gitProperties["git.commit.id"].toString(),
            commitIdAbbrev = gitProperties["git.commit.id.abbrev"].toString(),
            commitTime = gitProperties["git.commit.time"].toString(),
        )
    }
}

data class Version(
    var git: Git = Git(),
)

data class Git(
    var branch: String = "unknown",
    var buildVersion: String = "unknown",
    var tag: String = "unknown",
    var commitId: String = "unknown",
    var commitIdAbbrev: String = "unknown",
    var commitTime: String = "unknown",
)

data class Validation(
    var password: Password = Password(),
    var username: Username = Username(),
)

data class Password(
    var length: PasswordLength = PasswordLength()
)

data class PasswordLength(
    var min: Int = 3,
    var max: Int = 255,
)

data class Username(
    var length: UsernameLength = UsernameLength()
)

data class UsernameLength(
    var max: Int = 255,
)

data class Database(
    var url: String = "jdbc:sqlite:pstorage.data.db",
    var username: String? = null,
    var password: String? = null,
)
data class FlywayProperties(
    var locations: String = "classpath:db/migrations",
)
data class UiProperties(
    var taskbarIconPath: String = "assets/icons/taskbar/icon64.png",
)