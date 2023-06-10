package com.github.shaart.pstorage.multiplatform.service.password

import com.github.shaart.pstorage.multiplatform.db.PasswordQueries
import com.github.shaart.pstorage.multiplatform.dto.PasswordViewDto
import com.github.shaart.pstorage.multiplatform.exception.AppException
import com.github.shaart.pstorage.multiplatform.model.Authentication
import com.github.shaart.pstorage.multiplatform.model.encryption.CryptoDto
import com.github.shaart.pstorage.multiplatform.service.encryption.EncryptionService
import com.github.shaart.pstorage.multiplatform.service.mapper.PasswordMapper

class DefaultPasswordService(
    private val passwordQueries: PasswordQueries,
    private val encryptionService: EncryptionService,
    private val passwordMapper: PasswordMapper,
) : PasswordService {
    override fun createPassword(
        authentication: Authentication,
        alias: String,
        rawPassword: String
    ): PasswordViewDto {
        val passwordEncryptionParam = CryptoDto(value = rawPassword, encryptionType = null)
        val currentUser = authentication.user
        val encryptionResult = encryptionService.encryptForUser(
            cryptoDto = passwordEncryptionParam,
            user = currentUser
        )
        val createdPassword = passwordQueries.transactionWithResult {
            passwordQueries.createPassword(
                userId = currentUser.id.toLong(),
                alias = alias,
                encryptedValue = encryptionResult.value,
                encryptionType = encryptionResult.encryptionType.toString()
            )
            val insertedId = passwordQueries.lastInsertRowId().executeAsOne()
            passwordQueries.findById(insertedId).executeAsOne()
        }
        return passwordMapper.entityToViewDto(createdPassword)
    }

    override fun deletePassword(alias: String, authentication: Authentication) {
        val affectedCount = passwordQueries.transactionWithResult {
            passwordQueries.deleteByAliasAndUserId(alias, authentication.user.id.toLong())
            passwordQueries.countAffectedRows()
        }.executeAsOne()
        if (affectedCount == 0L) {
            throw AppException("Cannot delete password with alias = '$alias' because password not found for current user")
        }
    }
}