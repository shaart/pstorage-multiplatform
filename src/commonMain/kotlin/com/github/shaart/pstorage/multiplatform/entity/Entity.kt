package com.github.shaart.pstorage.multiplatform.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    @SerialName("id")
    val id: Int? = null,
    @SerialName("name")
    val name: String,
    @SerialName("master_password")
    val masterPassword: String,
    @SerialName("encryption_type")
    val encryptionType: EncryptionType,
    @SerialName("role")
    val role: Role,
    @SerialName("created_at")
    val createdAt: String
) {

}

@Serializable
data class Role(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("created_at")
    val createdAt: String
)


@Serializable
data class Password(
    @SerialName("id")
    val id: Int? = null,
    @SerialName("user")
    val user: User,
    @SerialName("alias")
    val alias: String,
    @SerialName("encryption_type")
    val encryptionType: EncryptionType,
    @SerialName("encrypted_value")
    val encryptedValue: String,
    @SerialName("created_at")
    val createdAt: String
)

@Serializable
enum class EncryptionType {
    AES_CODER
}
