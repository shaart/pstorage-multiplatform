package com.github.shaart.pstorage.multiplatform.config

data class PstorageProperties(
    var applicationName: String = "PStorage",
    var applicationVersion: String = "1.0.0",
    var validation: Validation = Validation(),
    var aes: Aes = Aes(),
    var database: Database = Database(),
    var flyway: FlywayProperties = FlywayProperties(),
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

data class Aes(
    var common: Common = Common(),
)

data class Common(
    var key: String = "@S7r0ng\$ecre7Key",
    var vector: String = "encRyb7!n1tVe70r",
)

data class Database(
    var url: String = "jdbc:sqlite:pstorage.data.db",
    var username: String? = null,
    var password: String? = null,
)
data class FlywayProperties(
    var locations: String = "classpath:db/migrations",
)