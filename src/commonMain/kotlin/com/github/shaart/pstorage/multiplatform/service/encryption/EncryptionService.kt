package com.github.shaart.pstorage.multiplatform.service.encryption

import com.github.shaart.pstorage.multiplatform.dto.UserViewDto
import com.github.shaart.pstorage.multiplatform.enums.EncryptionType
import com.github.shaart.pstorage.multiplatform.model.encryption.CryptoDto
import com.github.shaart.pstorage.multiplatform.model.encryption.CryptoResult

/**
 * An encryption service for encrypt-decrypt operations.
 */
interface EncryptionService {
    /**
     * Encrypts a value using default key.
     *
     * @param cryptoDto a value to be encrypted
     * @return encrypted value
     */
    fun encryptForInMemory(cryptoDto: CryptoDto): CryptoResult

    /**
     * Encrypts a value using key.
     *
     * @param cryptoDto a value to be encrypted
     * @param key           an encryption key
     * @return encrypted value
     */
    fun encrypt(cryptoDto: CryptoDto, key: String): CryptoResult

    /**
     * Encrypts a value using user's master password as a key.
     *
     * @param cryptoDto a value to be encrypted
     * @param user          a user with master password
     * @return encrypted value
     */
    fun encryptForUser(cryptoDto: CryptoDto, user: UserViewDto): CryptoResult

    /**
     * Decrypts a value using default key.
     *
     * @param cryptoDto a value to be decrypted
     * @return decrypted value
     */
    fun decryptForInMemory(cryptoDto: CryptoDto): CryptoResult

    /**
     * Decrypts a value using key.
     *
     * @param cryptoDto a value to be decrypted
     * @return decrypted value
     */
    fun decrypt(cryptoDto: CryptoDto, key: String): CryptoResult

    /**
     * Decrypts a value using user's master password as a key.
     *
     * @param value a value to be decrypted
     * @param encryptedMasterPassword a user's master password
     * @return decrypted value
     */
    fun decryptForUser(value: CryptoDto, encryptionType: EncryptionType, encryptedMasterPassword: String): CryptoResult

    /**
     * Calculates a hash for value.
     *
     * @param value a raw input value
     * @return value's hash
     */
    fun calculateHash(value: String): String

    /**
     * Check if [rawValue] matches [expectedHashValue].
     *
     * @param rawValue raw input value. For example, raw password
     * @param expectedHashValue hash value to match
     * @return true of [rawValue]'s hash matches [expectedHashValue]
     */
    fun matchesHash(rawValue: String, expectedHashValue: String): Boolean
}
