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
        val isExistsInDatabase = passwordQueries.existsByAliasAndUserId(
            alias = alias,
            userId = authentication.user.id.toLong()
        ).executeAsOne()
        if (isExistsInDatabase) {
            throw AppException("User already have password with alias = '$alias'")
        }

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

    override fun updatePasswordValue(
        password: PasswordViewDto,
        newPasswordValue: String,
        authentication: Authentication
    ): PasswordViewDto {
        val encryptionResult = encryptionService.encryptForUser(
            cryptoDto = CryptoDto(value = newPasswordValue, encryptionType = null),
            user = authentication.user,
        )
        val alias = password.alias
        val affectedCount = passwordQueries.transactionWithResult {
            passwordQueries.updateValueByUserIdAndOldAlias(
                encryptedValue = encryptionResult.value,
                encryptionType = encryptionResult.encryptionType.toString(),
                userId = authentication.user.id.toLong(),
                alias = alias,
            )
            passwordQueries.countAffectedRows()
        }.executeAsOne()
        if (affectedCount == 0L) {
            throw AppException("Cannot update password with alias = '$alias' because password not found for current user")
        }
        return password.copy(
            copyValue = passwordMapper.createCopyFunction(
                encryptedValue = encryptionResult.value,
                encryptionType = encryptionResult.encryptionType,
            )
        )
    }

    override fun updateAlias(
        password: PasswordViewDto,
        newAliasValue: String,
        authentication: Authentication
    ): PasswordViewDto {
        val alias = password.alias
        val affectedCount = passwordQueries.transactionWithResult {
            passwordQueries.updateAliasByUserIdAndOldAlias(
                userId = authentication.user.id.toLong(),
                alias = alias,
                newAlias = newAliasValue,
            )
            passwordQueries.countAffectedRows()
        }.executeAsOne()
        if (affectedCount == 0L) {
            throw AppException("Cannot update password's alias for alias = '$alias' because password not found for current user")
        }
        return password.copy(
            alias = newAliasValue
        )
    }
}