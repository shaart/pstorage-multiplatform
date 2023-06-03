package com.github.shaart.pstorage.multiplatform.config

import com.github.shaart.pstorage.multiplatform.db.PstorageDatabase
import com.github.shaart.pstorage.multiplatform.service.auth.AuthService
import com.github.shaart.pstorage.multiplatform.service.encryption.coder.AesCoder
import com.github.shaart.pstorage.multiplatform.service.encryption.EncryptionService
import com.github.shaart.pstorage.multiplatform.service.encryption.EncryptionServiceImpl
import org.flywaydb.core.Flyway

class AppConfig {

    companion object {
        public val properties = PstorageProperties()
        private val databaseDriverFactory = DatabaseDriverFactory()
        private val sqlDriver = databaseDriverFactory.createDriver(properties)
        private val database = PstorageDatabase(sqlDriver)

        private val coder = AesCoder()
        private val encryptionService: EncryptionService = EncryptionServiceImpl(properties, coder)
        private val authService = AuthService(
            userQueries = database.userQueries,
            encryptionService = encryptionService,
        )

        fun init(): AppContext {
            migrateDatabase()
            return AppContext(
                authService = authService,
                database = database,
                properties = properties,
            )
        }

        private fun migrateDatabase() {
            val flyway = Flyway.configure()
                .dataSource(
                    properties.database.url,
                    properties.database.username,
                    properties.database.password,
                )
                .locations(properties.flyway.locations)
                .load()
            flyway.migrate()
        }
    }
}