package com.github.shaart.pstorage.multiplatform.dto

import com.github.shaart.pstorage.multiplatform.entity.EncryptionType
import java.time.LocalDateTime

data class UserViewDto(
    var id: String,
    val name: String,
    val masterPassword: String,
    val encryptionType: EncryptionType,
    val role: RoleViewDto,
    val createdAt: LocalDateTime,
    val passwords: List<PasswordViewDto>
)