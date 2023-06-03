package com.github.shaart.pstorage.multiplatform.model.encryption

import com.github.shaart.pstorage.multiplatform.entity.EncryptionType

data class CryptoResult(
    val value: String,
    val encryptionType: EncryptionType
)
