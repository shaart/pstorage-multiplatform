package com.github.shaart.pstorage.multiplatform.service.auth

import com.github.shaart.pstorage.multiplatform.db.PasswordQueries
import com.github.shaart.pstorage.multiplatform.db.RoleQueries
import com.github.shaart.pstorage.multiplatform.db.UserQueries
import com.github.shaart.pstorage.multiplatform.db.UserSettingQueries
import com.github.shaart.pstorage.multiplatform.dto.UserViewDto
import com.github.shaart.pstorage.multiplatform.enums.EncryptionType
import com.github.shaart.pstorage.multiplatform.exception.AppException
import com.github.shaart.pstorage.multiplatform.exception.AuthNoMatchingUserException
import com.github.shaart.pstorage.multiplatform.logger
import com.github.shaart.pstorage.multiplatform.model.LoginModel
import com.github.shaart.pstorage.multiplatform.model.RegisterModel
import com.github.shaart.pstorage.multiplatform.model.encryption.CryptoDto
import com.github.shaart.pstorage.multiplatform.model.encryption.CryptoResult
import com.github.shaart.pstorage.multiplatform.service.encryption.EncryptionService
import com.github.shaart.pstorage.multiplatform.service.mapper.UserMapper
import com.github.shaart.pstorage.multiplatform.service.mask.Masker
import migrations.Usr_users

class DefaultAuthService(
    private val userQueries: UserQueries,
    private val passwordQueries: PasswordQueries,
    private val userSettingQueries: UserSettingQueries,
    private val roleQueries: RoleQueries,
    private val encryptionService: EncryptionService,
    private val userMapper: UserMapper,
    private val masker: Masker,
) : AuthService {

    private val log = logger()

    override fun register(registerModel: RegisterModel): UserViewDto {
        log.info("Registering user with name = '{}'", masker.username(registerModel.login))
        validateRegistrationRequest(registerModel)

        val createdUser = userQueries.transactionWithResult {
            val passwordsHash = encryptionService.calculateHash(registerModel.password)
            userQueries.createUser(
                name = registerModel.login,
                masterPassword = passwordsHash,
                encryptionType = EncryptionType.Constants.HASH,
            )
            val insertedId = userQueries.lastInsertRowId().executeAsOne()
            userQueries.findUserById(insertedId).executeAsOne()
        }
        val cryptoDto = CryptoDto(
            value = registerModel.password,
            encryptionType = null
        )
        val encryptedMasterPassword = encryptionService.encryptForInMemory(cryptoDto)
        return enrichToDto(createdUser, encryptedMasterPassword).also {
            log.info(
                "Successfully registered user with name = '{}'",
                masker.username(registerModel.login)
            )
        }
    }

    private fun validateRegistrationRequest(registerModel: RegisterModel) {
        if (registerModel.isNonMatchingPasswords()) {
            throw AppException("'Password' and 'Confirmation password' are different")
        }

        val existsUserByName = userQueries.existsUserByName(registerModel.login).executeAsOne()
        if (existsUserByName) {
            throw AppException("User with that name already present")
        }
    }

    override fun login(loginModel: LoginModel): UserViewDto {
        log.info("Logging in to user = '{}'", masker.username(loginModel.login))
        val user = userQueries.findUserByName(loginModel.login).executeAsOneOrNull()
            ?: throw AuthNoMatchingUserException()

        if (user.encrypt_type == EncryptionType.AES_CODER.name) {
            log.error("Found legacy user (id={}) from app's pre-alpha version", user.id)
            throw AppException("Cannot perform login action for requested values")
        }
        if (!encryptionService.matchesHash(
                rawValue = loginModel.password,
                expectedHashValue = user.master_password
            )
        ) {
            throw AuthNoMatchingUserException()
        }

        val cryptoDto = CryptoDto(
            value = loginModel.password,
            encryptionType = null,
        )
        val encryptedRequestPassword = encryptionService.encryptForInMemory(cryptoDto)
        return enrichToDto(user, encryptedRequestPassword).also {
            log.info("Successful log in to user = '{}'", masker.username(loginModel.login))
        }
    }

    private fun enrichToDto(user: Usr_users, encryptedMasterPassword: CryptoResult): UserViewDto {
        val passwords = passwordQueries.findAllByUserId(user.id).executeAsList()
        val role = roleQueries.findRoleById(user.role_id).executeAsOne()
        val settings =  userSettingQueries.findAllSettingsByUserId(user.id).executeAsList()
        return userMapper.entityToViewDto(
            user = user,
            passwords = passwords,
            role = role,
            encryptedMasterPassword = encryptedMasterPassword,
            settings = settings,
        )
    }
}