package com.github.shaart.pstorage.multiplatform.config

import com.github.shaart.pstorage.multiplatform.db.PstorageDatabase
import com.github.shaart.pstorage.multiplatform.enums.EncryptionType
import com.github.shaart.pstorage.multiplatform.exception.GlobalExceptionHandler
import com.github.shaart.pstorage.multiplatform.logger
import com.github.shaart.pstorage.multiplatform.service.auth.DefaultAuthService
import com.github.shaart.pstorage.multiplatform.service.encryption.EncryptionService
import com.github.shaart.pstorage.multiplatform.service.encryption.EncryptionServiceImpl
import com.github.shaart.pstorage.multiplatform.service.encryption.coder.AesCoder
import com.github.shaart.pstorage.multiplatform.service.mapper.PasswordMapper
import com.github.shaart.pstorage.multiplatform.service.mapper.RoleMapper
import com.github.shaart.pstorage.multiplatform.service.mapper.UserMapper
import com.github.shaart.pstorage.multiplatform.service.password.DefaultPasswordService
import com.github.shaart.pstorage.multiplatform.service.password.PasswordService
import org.flywaydb.core.Flyway
import java.util.*

class AppConfig {

    companion object {
        private val log = logger()

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

        private val authService = DefaultAuthService(
            userQueries = database.userQueries,
            passwordQueries = database.passwordQueries,
            roleQueries = database.roleQueries,
            encryptionService = encryptionService,
            userMapper = userMapper,
        )
        private val passwordService: PasswordService = DefaultPasswordService(
            passwordQueries = database.passwordQueries,
            encryptionService = encryptionService,
            passwordMapper = passwordMapper,
        )

        fun init(isMigrateDatabase: Boolean): ApplicationContext {
            if (isMigrateDatabase) {
                log.info("Migrating database...")
                migrateDatabase()
            }
            return DefaultApplicationContext(
                authService = authService,
                properties = properties,
                globalExceptionHandler = globalExceptionHandler,
                passwordService = passwordService
            )
        }

        private fun migrateDatabase() {
            val flyway = Flyway.configure()
                .dataSource(
                    properties.database.url,
                    properties.database.username,
                    properties.database.password,
                )
                .loggers("slf4j")
                .locations(properties.flyway.locations)
                .load()
            flyway.migrate()
        }
    }
}