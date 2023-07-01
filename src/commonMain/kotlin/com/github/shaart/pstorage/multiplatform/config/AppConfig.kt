package com.github.shaart.pstorage.multiplatform.config

import com.github.shaart.pstorage.multiplatform.context.DefaultSecurityContext
import com.github.shaart.pstorage.multiplatform.context.SecurityContext
import com.github.shaart.pstorage.multiplatform.db.PstorageDatabase
import com.github.shaart.pstorage.multiplatform.enums.EncryptionType
import com.github.shaart.pstorage.multiplatform.exception.GlobalExceptionHandler
import com.github.shaart.pstorage.multiplatform.generator.DefaultPasswordGenerator
import com.github.shaart.pstorage.multiplatform.generator.PasswordGenerator
import com.github.shaart.pstorage.multiplatform.logger
import com.github.shaart.pstorage.multiplatform.service.auth.DefaultAuthService
import com.github.shaart.pstorage.multiplatform.service.encryption.EncryptionService
import com.github.shaart.pstorage.multiplatform.service.encryption.EncryptionServiceImpl
import com.github.shaart.pstorage.multiplatform.service.encryption.coder.AesCoder
import com.github.shaart.pstorage.multiplatform.service.mapper.PasswordMapper
import com.github.shaart.pstorage.multiplatform.service.mapper.RoleMapper
import com.github.shaart.pstorage.multiplatform.service.mapper.UserMapper
import com.github.shaart.pstorage.multiplatform.service.mapper.UserSettingsMapper
import com.github.shaart.pstorage.multiplatform.service.mask.DefaultMasker
import com.github.shaart.pstorage.multiplatform.service.password.DefaultPasswordService
import com.github.shaart.pstorage.multiplatform.service.password.PasswordService
import com.github.shaart.pstorage.multiplatform.service.setting.DefaultSettingsService
import com.github.shaart.pstorage.multiplatform.service.setting.SettingsService
import org.flywaydb.core.Flyway
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.util.*

class AppConfig {

    companion object {
        private val log = logger()

        private val properties = PstorageProperties().also {
            val gitProperties = Properties()
            val gitPropertiesStream =
                AppConfig::class.java.classLoader.getResourceAsStream("git.properties")
            if (gitPropertiesStream != null) {
                gitProperties.load(gitPropertiesStream)
                it.populate(gitProperties)
            }
        }
        private val databaseDriverFactory = DatabaseDriverFactory()
        private val sqlDriver = databaseDriverFactory.createDriver(properties)
        private val database = PstorageDatabase(sqlDriver)

        private val globalExceptionHandler = GlobalExceptionHandler(properties)
        private val aesCoder = AesCoder()
        private val bcryptPasswordEncoder = BCryptPasswordEncoder()
        private val passwordGenerator: PasswordGenerator = DefaultPasswordGenerator()

        private val securityContext: SecurityContext = DefaultSecurityContext(
            currentEncodingKey = passwordGenerator.generateSecureRandomPassword(),
        )

        private val encryptionService: EncryptionService = EncryptionServiceImpl(
            coders = listOf(
                aesCoder
            ).associateByTo(EnumMap(EncryptionType::class.java)) { it.getEncryptionType() },
            defaultEncryptionType = EncryptionType.AES_CODER,
            passwordEncoder = bcryptPasswordEncoder,
            securityContext = securityContext
        )

        private val roleMapper = RoleMapper()
        private val passwordMapper = PasswordMapper(encryptionService)
        private val userSettingsMapper = UserSettingsMapper()

        private val userMapper = UserMapper(
            roleMapper = roleMapper,
            passwordMapper = passwordMapper,
            userSettingsMapper = userSettingsMapper,
        )

        private val masker = DefaultMasker()
        private val authService = DefaultAuthService(
            userQueries = database.userQueries,
            passwordQueries = database.passwordQueries,
            userSettingQueries = database.userSettingQueries,
            roleQueries = database.roleQueries,
            encryptionService = encryptionService,
            userMapper = userMapper,
            masker = masker,
        )
        private val passwordService: PasswordService = DefaultPasswordService(
            passwordQueries = database.passwordQueries,
            encryptionService = encryptionService,
            passwordMapper = passwordMapper,
            masker = masker,
        )
        private val settingsService: SettingsService = DefaultSettingsService(
            settingQueries = database.userSettingQueries,
            settingsMapper = userSettingsMapper,
        )

        fun init(isMigrateDatabase: Boolean): ApplicationContext {
            log.info("Loading application with version: {}", properties.version)
            if (isMigrateDatabase) {
                log.info("Migrating database...")
                migrateDatabase()
            }
            return DefaultApplicationContext(
                authService = authService,
                settingsService = settingsService,
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
            if (log.isDebugEnabled) {
                log.debug(
                    "Found migrations in '{}': {}",
                    properties.flyway.locations,
                    flyway.info().all().map { it.script }
                )
            }
            flyway.migrate()
        }
    }
}