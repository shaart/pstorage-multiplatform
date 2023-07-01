package com.github.shaart.pstorage.multiplatform.service.mapper

import com.github.shaart.pstorage.multiplatform.dto.UserViewDto
import com.github.shaart.pstorage.multiplatform.model.encryption.CryptoResult
import migrations.Dct_roles
import migrations.Usr_passwords
import migrations.Usr_settings
import migrations.Usr_users
import java.time.LocalDateTime

class UserMapper(
    private val roleMapper: RoleMapper,
    private val passwordMapper: PasswordMapper,
    private val userSettingsMapper: UserSettingsMapper,
) {
    fun entityToViewDto(
        user: Usr_users,
        passwords: List<Usr_passwords>,
        role: Dct_roles,
        encryptedMasterPassword: CryptoResult,
        settings: List<Usr_settings>,
    ): UserViewDto {
        return UserViewDto(
            id = user.id.toString(),
            name = user.name,
            masterPassword = encryptedMasterPassword.value,
            encryptionType = encryptedMasterPassword.encryptionType,
            role = roleMapper.entityToViewDto(role),
            createdAt = LocalDateTime.parse(user.created_at),
            passwords = passwords.map { passwordMapper.entityToViewDto(it) },
            settings = userSettingsMapper.entityToViewDtoWithDefaultSettings(settings)
        )
    }
}
