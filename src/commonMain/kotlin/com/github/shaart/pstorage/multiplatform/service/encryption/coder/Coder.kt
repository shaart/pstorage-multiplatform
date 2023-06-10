package com.github.shaart.pstorage.multiplatform.service.encryption.coder

import com.github.shaart.pstorage.multiplatform.enums.EncryptionType

/**
 * Component for encrypting and decrypting with some algorithm.
 */
interface Coder {
    /**
     * Decrypts info with a key.
     *
     * @param toBeDecrypted Encrypted information
     * @param key           A key that was used during encryption
     * @return decrypted value
     * @throws com.github.shaart.pstorage.multiplatform.exception.CryptoException on error
     */
    fun decrypt(toBeDecrypted: String, key: String): String

    /**
     * Encrypt value with a key.
     *
     * @param toBeEncrypted Initial value
     * @param key           A key
     * @return encrypted value
     * @throws com.github.shaart.pstorage.multiplatform.exception.CryptoException on error
     */
    fun encrypt(toBeEncrypted: String, key: String): String

    /**
     * Get encryption type of current coder.
     */
    fun getEncryptionType(): EncryptionType
}
