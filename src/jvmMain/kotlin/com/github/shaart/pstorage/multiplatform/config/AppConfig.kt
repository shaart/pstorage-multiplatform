package com.github.shaart.pstorage.multiplatform.config

import com.github.shaart.pstorage.multiplatform.DatabaseDriverFactory
import com.github.shaart.pstorage.multiplatform.db.PstorageDatabase
import com.github.shaart.pstorage.multiplatform.service.AuthService
import org.flywaydb.core.Flyway

class AppConfig {

    companion object {
        private val databaseDriverFactory = DatabaseDriverFactory()
        private val sqlDriver = databaseDriverFactory.createDriver()
        private val database = PstorageDatabase(sqlDriver)
        private val authService = AuthService(database.userQueries)

        fun init(): AppContext {
            migrateDatabase()
            return AppContext(
                authService = authService,
                database = database
            )
        }

        private fun migrateDatabase() {
            val flyway = Flyway.configure()
                .dataSource(getDatabaseUrl(), getDatabaseUser(), getDatabasePassword())
                .locations("classpath:db/migrations")
                .load()
            flyway.migrate()
        }

        private fun getDatabasePassword() = null
        private fun getDatabaseUser() = null
        fun getDatabaseUrl() = "jdbc:sqlite:pstorage.data.db"
    }
}