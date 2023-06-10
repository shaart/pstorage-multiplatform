package com.github.shaart.pstorage.multiplatform.service.mapper

import com.github.shaart.pstorage.multiplatform.dto.PasswordViewDto
import com.github.shaart.pstorage.multiplatform.enums.EncryptionType
import com.github.shaart.pstorage.multiplatform.model.Authentication
import com.github.shaart.pstorage.multiplatform.model.encryption.CryptoDto
import com.github.shaart.pstorage.multiplatform.service.encryption.EncryptionService
import com.github.shaart.pstorage.multiplatform.util.ClipboardUtil
import migrations.Usr_passwords

class PasswordMapper(
    private val encryptionService: EncryptionService
) {
    fun entityToViewDto(password: Usr_passwords): PasswordViewDto {
        return PasswordViewDto(
            alias = password.alias,
            copyValue = createCopyFunction(
                encryptedValue = password.encrypted_value,
                encryptionType = EncryptionType.valueOf(password.encrypt_type),
            )
        )
    }

    fun createCopyFunction(
        encryptedValue: String,
        encryptionType: EncryptionType,
    ): (Authentication) -> Unit {
        return {
            val decryptionResult = encryptionService.decryptForUser(
                value = CryptoDto(
                    value = encryptedValue,
                    encryptionType = encryptionType,
                ),
                encryptedMasterPassword = it.user.masterPassword,
                encryptionType = it.user.encryptionType,
            )
            ClipboardUtil.setValueToClipboard(decryptionResult.value)
        }
    }
}
