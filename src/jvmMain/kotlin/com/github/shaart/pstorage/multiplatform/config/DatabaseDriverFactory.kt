package com.github.shaart.pstorage.multiplatform.config

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver

actual class DatabaseDriverFactory {
    actual fun createDriver(properties: PstorageProperties): SqlDriver {
        return JdbcSqliteDriver(properties.database.url)
    }
}