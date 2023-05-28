package com.github.shaart.pstorage.multiplatform.config

import com.github.shaart.pstorage.multiplatform.DatabaseDriverFactory
import com.github.shaart.pstorage.multiplatform.db.PstorageDatabase
import com.github.shaart.pstorage.multiplatform.service.AuthService

// TODO https://www.47deg.com/blog/sqldelight-flyway/ do i need it?
// TODO https://kotlinlang.org/docs/multiplatform-mobile-ktor-sqldelight.html#implement-the-presentation-logic
class AppConfig {

    companion object {
        private val databaseDriverFactory = DatabaseDriverFactory()
        private val database = PstorageDatabase(databaseDriverFactory.createDriver())
        private val authService = AuthService(database.userQueries)

        fun init(): AppContext {
            database.Schema.
            return AppContext(
                authService = authService,
                database = database
            )
        }

        fun getDatabaseUrl(): String {
            return "jdbc:sqlite:pstorage.data.db"
        }
    }
}