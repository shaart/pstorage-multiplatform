package com.github.shaart.pstorage.multiplatform.service.password

import com.github.shaart.pstorage.multiplatform.db.PasswordQueries
import com.github.shaart.pstorage.multiplatform.dto.PasswordViewDto
import com.github.shaart.pstorage.multiplatform.exception.AppException
import com.github.shaart.pstorage.multiplatform.logger
import com.github.shaart.pstorage.multiplatform.model.Authentication
import com.github.shaart.pstorage.multiplatform.model.encryption.CryptoDto
import com.github.shaart.pstorage.multiplatform.service.encryption.EncryptionService
import com.github.shaart.pstorage.multiplatform.service.mapper.PasswordMapper
import com.github.shaart.pstorage.multiplatform.service.mask.Masker

class DefaultPasswordService(
    private val passwordQueries: PasswordQueries,
    private val encryptionService: EncryptionService,
    private val passwordMapper: PasswordMapper,
    private val masker: Masker,
) : PasswordService {

    private val log = logger()

    override fun createPassword(
        authentication: Authentication,
        alias: String,
        rawPassword: String
    ): PasswordViewDto {
        log.info("Creating password with alias = '{}'", masker.alias(alias))
        if (alias.isBlank()) {
            throw AppException("Alias should not be blank")
        }
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
        return passwordMapper.entityToViewDto(createdPassword).also {
            log.info("Successfully created password with alias = '{}'", masker.alias(alias))
        }
    }

    override fun deletePassword(alias: String, authentication: Authentication) {
        log.info("Deleting password with alias = '{}'", masker.alias(alias))
        val affectedCount = passwordQueries.transactionWithResult {
            passwordQueries.deleteByAliasAndUserId(alias, authentication.user.id.toLong())
            passwordQueries.countAffectedRows()
        }.executeAsOne()
        if (affectedCount == 0L) {
            throw AppException("Cannot delete password with alias = '$alias' because password not found for current user")
        }
        log.info("Successfully deleted password with alias = '{}'", masker.alias(alias))
    }

    override fun updatePasswordValue(
        password: PasswordViewDto,
        newPasswordValue: String,
        authentication: Authentication
    ): PasswordViewDto {
        log.info("Updating value for password with alias = '{}'", masker.alias(password.alias))
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
        ).also {
            log.info(
                "Successfully updated value for password with alias = '{}'",
                masker.alias(password.alias)
            )
        }
    }

    override fun updateAlias(
        password: PasswordViewDto,
        newAliasValue: String,
        authentication: Authentication
    ): PasswordViewDto {
        log.info(
            "Updating alias for password with alias = '{}' to '{}'",
            masker.alias(password.alias),
            masker.alias(newAliasValue)
        )
        if (newAliasValue.isBlank()) {
            throw AppException("Alias should not be blank")
        }
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
        ).also {
            log.info(
                "Successfully updated alias for password with alias = '{}' to '{}'",
                masker.alias(password.alias),
                masker.alias(newAliasValue)
            )
        }
    }
}