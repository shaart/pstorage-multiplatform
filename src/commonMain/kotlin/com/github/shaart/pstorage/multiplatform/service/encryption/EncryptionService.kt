package com.github.shaart.pstorage.multiplatform.service.encryption

import com.github.shaart.pstorage.multiplatform.dto.UserDto
import com.github.shaart.pstorage.multiplatform.entity.EncryptionType
import com.github.shaart.pstorage.multiplatform.model.encryption.CryptoDto
import com.github.shaart.pstorage.multiplatform.model.encryption.CryptoResult

/**
 * An encryption service for encrypt-decrypt operations.
 */
interface EncryptionService {
    /**
     * Encrypts a value using default key.
     *
     * @param encryptionDto a value to be encrypted
     * @return encrypted value
     */
    fun encrypt(encryptionDto: CryptoDto): CryptoResult

    /**
     * Encrypts a value using key.
     *
     * @param encryptionDto a value to be encrypted
     * @param key           an encryption key
     * @return encrypted value
     */
    fun encrypt(encryptionDto: CryptoDto, key: String): CryptoResult

    /**
     * Encrypts a value using user's master password as a key.
     *
     * @param encryptionDto a value to be encrypted
     * @param user          a user with master password
     * @return encrypted value
     */
    fun encryptForUser(encryptionDto: CryptoDto, user: UserDto): CryptoResult

    /**
     * Decrypts a value using default key.
     *
     * @param value a value to be decrypted
     * @return decrypted value
     */
    fun decrypt(value: CryptoDto): CryptoResult

    /**
     * Decrypts a value using key.
     *
     * @param value a value to be decrypted
     * @return decrypted value
     */
    fun decrypt(value: CryptoDto, key: String): CryptoResult
    fun decrypt(encryptionType: EncryptionType, cryptoDto: CryptoDto, key: String): CryptoResult

    /**
     * Encrypts a value using user's master password as a key.
     *
     * @param value a value to be decrypted
     * @param user  a user with master password
     * @return decrypted value
     */
    fun decryptForUser(value: CryptoDto, user: UserDto): CryptoResult
}
