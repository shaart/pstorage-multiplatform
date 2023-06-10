package com.github.shaart.pstorage.multiplatform.service.mapper

import com.github.shaart.pstorage.multiplatform.dto.UserViewDto
import com.github.shaart.pstorage.multiplatform.enums.EncryptionType
import migrations.Dct_roles
import migrations.Usr_passwords
import migrations.Usr_users
import java.time.LocalDateTime

class UserMapper(
    private val roleMapper: RoleMapper,
    private val passwordMapper: PasswordMapper,
) {
    fun entityToViewDto(
        user: Usr_users,
        passwords: List<Usr_passwords>,
        role: Dct_roles
    ): UserViewDto {
        return UserViewDto(
            id = user.id.toString(),
            name = user.name,
            masterPassword = user.master_password,
            encryptionType = EncryptionType.valueOf(user.encrypt_type),
            role = roleMapper.entityToViewDto(role),
            createdAt = LocalDateTime.parse(user.created_at),
            passwords = passwords.map { passwordMapper.entityToViewDto(it) },
        )
    }
}
