package com.github.shaart.pstorage.multiplatform.service.auth

import com.github.shaart.pstorage.multiplatform.db.PasswordQueries
import com.github.shaart.pstorage.multiplatform.db.RoleQueries
import com.github.shaart.pstorage.multiplatform.db.UserQueries
import com.github.shaart.pstorage.multiplatform.dto.UserViewDto
import com.github.shaart.pstorage.multiplatform.entity.EncryptionType
import com.github.shaart.pstorage.multiplatform.exception.AuthNoMatchingUserException
import com.github.shaart.pstorage.multiplatform.model.LoginModel
import com.github.shaart.pstorage.multiplatform.model.RegisterModel
import com.github.shaart.pstorage.multiplatform.model.encryption.CryptoDto
import com.github.shaart.pstorage.multiplatform.service.encryption.EncryptionService
import com.github.shaart.pstorage.multiplatform.service.mapper.UserMapper
import migrations.Usr_users

class DefaultAuthService(
    private val userQueries: UserQueries,
    private val passwordQueries: PasswordQueries,
    private val roleQueries: RoleQueries,
    private val encryptionService: EncryptionService,
    private val userMapper: UserMapper,
) : AuthService {
    override fun register(registerModel: RegisterModel): UserViewDto {
        val createdUser = userQueries.transactionWithResult {
            val cryptoDto = CryptoDto(
                value = registerModel.password,
                encryptionType = null
            )
            val encryptedMasterPassword = encryptionService.encrypt(cryptoDto)
            userQueries.createUser(
                name = registerModel.login,
                masterPassword = encryptedMasterPassword.value,
                encryptionType = encryptedMasterPassword.encryptionType.name
            )
            val insertedId = userQueries.lastInsertRowId().executeAsOne()
            userQueries.findUserById(insertedId).executeAsOne()
        }
        return enrichToDto(createdUser)
    }

    override fun login(loginModel: LoginModel): UserViewDto {
        val user = userQueries.findUserByName(loginModel.login).executeAsOneOrNull()
            ?: throw AuthNoMatchingUserException()

        val cryptoDto = CryptoDto(
            value = loginModel.password,
            encryptionType = EncryptionType.valueOf(user.encrypt_type)
        )
        val encryptedRequestPassword = encryptionService.encrypt(cryptoDto).value
        if (encryptedRequestPassword != user.master_password) {
            throw AuthNoMatchingUserException()
        }
        return enrichToDto(user)
    }

    private fun enrichToDto(user: Usr_users): UserViewDto {
        val passwords = passwordQueries.findAllByUserId(user.id).executeAsList()
        val role = roleQueries.findRoleById(user.role_id).executeAsOne()
        return userMapper.entityToViewDto(
            user = user,
            passwords = passwords,
            role = role
        )
    }
}