package com.github.shaart.pstorage.multiplatform.model.encryption

import com.github.shaart.pstorage.multiplatform.enums.EncryptionType

data class CryptoResult(
    val value: String,
    val encryptionType: EncryptionType
)
