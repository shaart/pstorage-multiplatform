package com.github.shaart.pstorage.multiplatform.service.encryption.coder

import com.github.shaart.pstorage.multiplatform.enums.EncryptionType
import com.github.shaart.pstorage.multiplatform.exception.CryptoException
import java.nio.charset.StandardCharsets
import java.security.spec.AlgorithmParameterSpec
import java.security.spec.InvalidKeySpecException
import java.security.spec.KeySpec
import java.util.*
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

class AesCoder : Coder {
    companion object {
        private const val PKBDF2_ALGO = "PBKDF2WithHmacSHA256"
        private const val CYPHER_ALGORITHM = "AES/CBC/PKCS5PADDING"
        private const val ITERATIONS = 10000
        private const val HASH_LENGTH_BYTES = 256
        private const val IV_LENGTH = 16
        private const val DUMMY_VALUE = 1
    }

    private var cipher: Cipher
    private var factory: SecretKeyFactory

    /**
     * Default way to create AesCoder.
     */
    init {
        try {
            cipher = Cipher.getInstance(CYPHER_ALGORITHM)
            factory = SecretKeyFactory.getInstance(PKBDF2_ALGO)
        } catch (e: Exception) {
            throw CryptoException("Cannot configure AesCoder", e)
        }
    }

    override fun decrypt(toBeDecrypted: String, key: String): String {
        val secureIv = generateSecureIv(key)
        val salt = generateSalt(key)
        val secretKey = generateEncryptionKey(key, salt)
        return decrypt(secretKey, secureIv, toBeDecrypted)
    }

    /**
     * Decrypt toBeDecrypted using the secret and passed iv.
     */
    private fun decrypt(
        secret: SecretKey,
        encodedIv: ByteArray,
        toBeDecrypted: String
    ): String {
        val iv = Base64.getDecoder().decode(encodedIv)
        val ivParameterSpec: AlgorithmParameterSpec = IvParameterSpec(iv)
        val decryptedValue: ByteArray = try {
            cipher.init(Cipher.DECRYPT_MODE, secret, ivParameterSpec)
            val decodedValue = Base64.getDecoder().decode(toBeDecrypted.toByteArray())
            cipher.doFinal(decodedValue)
        } catch (e: Exception) {
            throw CryptoException("Cannot decrypt value", e)
        }
        return String(decryptedValue)
    }

    override fun encrypt(toBeEncrypted: String, key: String): String {
        val salt = generateSalt(key)
        val secretKey = generateEncryptionKey(key, salt)
        val encodedIv = generateSecureIv(key)
        return encrypt(secretKey, encodedIv, toBeEncrypted)
    }

    /**
     * Encrypt given toBeEncrypted with passed SecretKey and IV.
     */
    private fun encrypt(secret: SecretKey, encodedIv: ByteArray, toBeEncrypted: String): String {
        val iv = Base64.getDecoder().decode(encodedIv)
        val ivParameterSpec: AlgorithmParameterSpec = IvParameterSpec(iv)
        val encryptedValue: ByteArray = try {
            cipher.init(Cipher.ENCRYPT_MODE, secret, ivParameterSpec)
            val toBeEncryptedBytes = toBeEncrypted.toByteArray(StandardCharsets.UTF_8)
            val cipherText = cipher.doFinal(toBeEncryptedBytes)
            Base64.getEncoder().encode(cipherText)
        } catch (e: Exception) {
            throw CryptoException("Cannot encrypt value", e)
        }
        return String(encryptedValue)
    }

    override fun getEncryptionType(): EncryptionType {
        return EncryptionType.AES_CODER
    }

    /**
     * Generate an encryption key using PBKDF2 with given salt, iterations and hash bytes.
     */
    private fun generateEncryptionKey(str: String, salt: ByteArray): SecretKey {
        val strChars = str.toCharArray()
        val spec: KeySpec = PBEKeySpec(strChars, salt, ITERATIONS, HASH_LENGTH_BYTES)
        val key: SecretKey
        return try {
            key = factory.generateSecret(spec)
            SecretKeySpec(key.encoded, "AES")
        } catch (e: InvalidKeySpecException) {
            throw CryptoException("Cannot generate encryption key", e)
        }
    }

    /**
     * Generates salt based on master password.
     *
     * @param masterPassword main password (key)
     * @return a salt
     */
    private fun generateSalt(masterPassword: String): ByteArray {
        return masterPassword.toByteArray(StandardCharsets.UTF_8)
    }

    /**
     * Generates secureIV using masterPassword.
     *
     * @param masterPassword main password (key)
     * @return a secure IV array
     */
    private fun generateSecureIv(masterPassword: String): ByteArray {
        var resultBytes = ByteArray(IV_LENGTH)
        val bytes = masterPassword.toByteArray(StandardCharsets.UTF_8)
        if (bytes.size > IV_LENGTH) {
            resultBytes = bytes.copyOfRange(0, IV_LENGTH)
        } else if (bytes.size < IV_LENGTH) {
            System.arraycopy(bytes, 0, resultBytes, 0, bytes.size)
            for (i in bytes.size until resultBytes.size) {
                resultBytes[i] = DUMMY_VALUE.toByte()
            }
        }
        return Base64.getEncoder().encode(resultBytes)
    }
}
