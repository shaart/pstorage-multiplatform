package com.github.shaart.pstorage.multiplatform.service.auth

import com.github.shaart.pstorage.multiplatform.db.UserQueries
import com.github.shaart.pstorage.multiplatform.exception.AuthNoMatchingUserException
import com.github.shaart.pstorage.multiplatform.model.LoginModel
import com.github.shaart.pstorage.multiplatform.model.RegisterModel
import com.github.shaart.pstorage.multiplatform.model.encryption.CryptoDto
import com.github.shaart.pstorage.multiplatform.service.encryption.EncryptionService
import migrations.Usr_users

class AuthService(
    private val userQueries: UserQueries,
    private val encryptionService: EncryptionService,
) {
    fun register(registerModel: RegisterModel): Usr_users {
        return userQueries.transactionWithResult {
            val cryptoDto = CryptoDto(registerModel.password)
            val encryptedMasterPassword = encryptionService.encrypt(cryptoDto)
            userQueries.createUser(
                name = registerModel.login,
                masterPassword = encryptedMasterPassword.value,
                encryptionType = encryptedMasterPassword.encryptionType.name
            )
            val insertedId = userQueries.lastInsertRowId().executeAsOne()
            userQueries.findUserById(insertedId).executeAsOne()
        }
    }

    fun login(loginModel: LoginModel): Usr_users {
        val cryptoDto = CryptoDto(loginModel.password)
        val encryptedMasterPassword = encryptionService.encrypt(cryptoDto).value
        return userQueries.findUserByNameAndPassword(loginModel.login, encryptedMasterPassword)
            .executeAsOneOrNull() ?: throw AuthNoMatchingUserException()
    }
}