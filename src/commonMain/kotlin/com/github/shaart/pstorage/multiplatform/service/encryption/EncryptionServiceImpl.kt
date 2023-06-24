package com.github.shaart.pstorage.multiplatform.service.encryption

import com.github.shaart.pstorage.multiplatform.config.PstorageProperties
import com.github.shaart.pstorage.multiplatform.dto.UserViewDto
import com.github.shaart.pstorage.multiplatform.enums.EncryptionType
import com.github.shaart.pstorage.multiplatform.exception.CryptoException
import com.github.shaart.pstorage.multiplatform.model.encryption.CryptoDto
import com.github.shaart.pstorage.multiplatform.model.encryption.CryptoResult
import com.github.shaart.pstorage.multiplatform.service.encryption.coder.Coder
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.*

class EncryptionServiceImpl(
    private val properties: PstorageProperties,
    private val coders: EnumMap<EncryptionType, Coder>,
    private val defaultEncryptionType: EncryptionType,
    private val passwordEncoder: PasswordEncoder,
) : EncryptionService {

    init {
        check(coders.containsKey(defaultEncryptionType)) {
            "Coder with type $defaultEncryptionType not found in map with Coders"
        }
        check(coders.keys.containsAll(EncryptionType.values().toList())) {
            "Received Coders don't satisfy all encryption types. Found coders: [${coders.keys}]"
        }
    }

    override fun encrypt(cryptoDto: CryptoDto): CryptoResult {
        val key: String = properties.aes.common.key
        return encrypt(cryptoDto, key)
    }

    override fun encrypt(cryptoDto: CryptoDto, key: String): CryptoResult {
        val encryptionType = cryptoDto.encryptionType ?: defaultEncryptionType
        val coder = coders[encryptionType]
            ?: throw CryptoException("Not found coder for type ${encryptionType.name}")
        return CryptoResult(
            value = coder.encrypt(cryptoDto.value, key),
            encryptionType = coder.getEncryptionType(),
        )
    }

    override fun encryptForUser(cryptoDto: CryptoDto, user: UserViewDto): CryptoResult {
        val masterPasswordParam = CryptoDto(
            value = user.masterPassword,
            encryptionType = user.encryptionType
        )
        val decryptedMasterPassword: String = decrypt(masterPasswordParam).value
        return encrypt(cryptoDto = cryptoDto, key = decryptedMasterPassword)
    }

    override fun decrypt(cryptoDto: CryptoDto): CryptoResult {
        val key: String = properties.aes.common.key
        return decrypt(cryptoDto = cryptoDto, key = key)
    }

    override fun decrypt(cryptoDto: CryptoDto, key: String): CryptoResult {
        val encryptionType = cryptoDto.encryptionType ?: defaultEncryptionType
        val coder = coders[encryptionType]
            ?: throw CryptoException("Not found coder for type ${encryptionType.name}")
        return CryptoResult(
            value = coder.decrypt(cryptoDto.value, key),
            encryptionType = coder.getEncryptionType(),
        )
    }

    override fun decryptForUser(
        value: CryptoDto,
        encryptionType: EncryptionType,
        encryptedMasterPassword: String
    ): CryptoResult {
        val masterPasswordParam = CryptoDto(
            value = encryptedMasterPassword,
            encryptionType = encryptionType
        )
        val decryptedMasterPassword: String = decrypt(masterPasswordParam).value
        return decrypt(
            cryptoDto = value,
            key = decryptedMasterPassword
        )
    }

    override fun calculateHash(value: String): String {
        return passwordEncoder.encode(value)
    }

    override fun matchesHash(rawValue: String, expectedHashValue: String): Boolean {
        return passwordEncoder.matches(rawValue, expectedHashValue)
    }
}
