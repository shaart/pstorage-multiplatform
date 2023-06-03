package com.github.shaart.pstorage.multiplatform.service.encryption

import com.github.shaart.pstorage.multiplatform.config.PstorageProperties
import com.github.shaart.pstorage.multiplatform.dto.UserDto
import com.github.shaart.pstorage.multiplatform.entity.EncryptionType
import com.github.shaart.pstorage.multiplatform.exception.CryptoException
import com.github.shaart.pstorage.multiplatform.model.encryption.CryptoDto
import com.github.shaart.pstorage.multiplatform.model.encryption.CryptoResult
import com.github.shaart.pstorage.multiplatform.service.encryption.coder.Coder
import java.util.*

class EncryptionServiceImpl(
    properties: PstorageProperties,
    coder: Coder
) : EncryptionService {
    private val properties: PstorageProperties
    private val coder: Coder
    private val codersMap: MutableMap<EncryptionType, Coder> = EnumMap(
        EncryptionType::class.java
    )

    /**
     * Default way to create EncryptionServiceImpl.
     */
    init {
        this.properties = properties
        this.coder = coder
        codersMap[coder.getEncryptionType()] = coder
    }

    override fun encrypt(encryptionDto: CryptoDto): CryptoResult {
        val key: String = properties.aes.common.key
        return encrypt(encryptionDto, key)
    }

    override fun encrypt(encryptionDto: CryptoDto, key: String): CryptoResult {
        return CryptoResult(
            value = coder.encrypt(encryptionDto.value, key),
            encryptionType = coder.getEncryptionType(),
        )
    }

    override fun encryptForUser(encryptionDto: CryptoDto, user: UserDto): CryptoResult {
        val userMasterPassword: String = user.masterPassword
        val passwordParam = CryptoDto(value = userMasterPassword)
        val decryptedMasterPassword: String = decrypt(passwordParam).value
        return encrypt(encryptionDto, decryptedMasterPassword)
    }

    override fun decrypt(value: CryptoDto): CryptoResult {
        val key: String = properties.aes.common.key
        return decrypt(value, key)
    }

    override fun decrypt(value: CryptoDto, key: String): CryptoResult {
        return CryptoResult(
            value = coder.decrypt(value.value, key),
            encryptionType = coder.getEncryptionType(),
        )
    }

    override fun decrypt(
        encryptionType: EncryptionType,
        cryptoDto: CryptoDto,
        key: String
    ): CryptoResult {
        val foundCoder: Coder = codersMap[encryptionType]
            ?: throw CryptoException("Not found coder for type " + encryptionType.name)
        return CryptoResult(
            value = foundCoder.decrypt(cryptoDto.value, key),
            encryptionType = coder.getEncryptionType()
        )
    }

    override fun decryptForUser(value: CryptoDto, user: UserDto): CryptoResult {
        val userMasterPassword: String = user.masterPassword
        val passwordParam = CryptoDto(value = userMasterPassword)
        val decryptedMasterPassword: String = decrypt(passwordParam).value
        return decrypt(value, decryptedMasterPassword)
    }
}
