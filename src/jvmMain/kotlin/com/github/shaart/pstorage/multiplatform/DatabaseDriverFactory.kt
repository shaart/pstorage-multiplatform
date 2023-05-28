package com.github.shaart.pstorage.multiplatform

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.github.shaart.pstorage.multiplatform.config.AppConfig

actual class DatabaseDriverFactory() {
    actual fun createDriver(): SqlDriver {
        return JdbcSqliteDriver(AppConfig.getDatabaseUrl())
    }
}