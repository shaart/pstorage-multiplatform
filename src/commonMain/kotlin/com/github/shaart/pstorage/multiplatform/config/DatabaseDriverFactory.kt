package com.github.shaart.pstorage.multiplatform.config

import app.cash.sqldelight.db.SqlDriver

expect class DatabaseDriverFactory() {
    fun createDriver(properties: PstorageProperties): SqlDriver
}