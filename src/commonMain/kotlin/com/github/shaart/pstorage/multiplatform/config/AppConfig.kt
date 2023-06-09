package com.github.shaart.pstorage.multiplatform.config

import com.github.shaart.pstorage.multiplatform.db.PstorageDatabase
import com.github.shaart.pstorage.multiplatform.entity.EncryptionType
import com.github.shaart.pstorage.multiplatform.exception.GlobalExceptionHandler
import com.github.shaart.pstorage.multiplatform.service.auth.AuthService
import com.github.shaart.pstorage.multiplatform.service.encryption.EncryptionService
import com.github.shaart.pstorage.multiplatform.service.encryption.EncryptionServiceImpl
import com.github.shaart.pstorage.multiplatform.service.encryption.coder.AesCoder
import com.github.shaart.pstorage.multiplatform.service.mapper.PasswordMapper
import com.github.shaart.pstorage.multiplatform.service.mapper.RoleMapper
import com.github.shaart.pstorage.multiplatform.service.mapper.UserMapper
import org.flywaydb.core.Flyway
import java.util.*

class AppConfig {

    companion object {
        private val properties = PstorageProperties()
        private val databaseDriverFactory = DatabaseDriverFactory()
        private val sqlDriver = databaseDriverFactory.createDriver(properties)
        private val database = PstorageDatabase(sqlDriver)

        private val globalExceptionHandler = GlobalExceptionHandler(properties)
        private val aesCoder = AesCoder()
        private val encryptionService: EncryptionService = EncryptionServiceImpl(
            properties = properties,
            defaultEncryptionType = EncryptionType.AES_CODER,
            coders = listOf(
                aesCoder
            ).associateByTo(EnumMap(EncryptionType::class.java)) { it.getEncryptionType() }
        )

        private val roleMapper = RoleMapper()
        private val passwordMapper = PasswordMapper(encryptionService)
        private val userMapper = UserMapper(roleMapper, passwordMapper)

        private val authService = AuthService(
            userQueries = database.userQueries,
            passwordQueries = database.passwordQueries,
            roleQueries = database.roleQueries,
            encryptionService = encryptionService,
            userMapper = userMapper,
        )

        fun init(isMigrateDatabase: Boolean): AppContext {
            if (isMigrateDatabase) {
                migrateDatabase()
            }
            return AppContext(
                authService = authService,
                database = database,
                properties = properties,
                globalExceptionHandler = globalExceptionHandler
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