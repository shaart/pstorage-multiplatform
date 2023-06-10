package com.github.shaart.pstorage.multiplatform.model.encryption

import com.github.shaart.pstorage.multiplatform.enums.EncryptionType

/**
 * Model for encryption/decryption operations argument.
 */
data class CryptoDto (
    val value: String,
    val encryptionType: EncryptionType?,
)