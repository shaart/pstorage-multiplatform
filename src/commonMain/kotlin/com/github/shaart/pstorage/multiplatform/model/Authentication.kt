package com.github.shaart.pstorage.multiplatform.model

import com.github.shaart.pstorage.multiplatform.dto.PasswordViewDto
import com.github.shaart.pstorage.multiplatform.dto.UserSettingViewDto
import com.github.shaart.pstorage.multiplatform.dto.UserViewDto

data class Authentication(
    val user: UserViewDto,
) {
    fun withPasswords(passwords: List<PasswordViewDto>): Authentication {
        return copy(
            user = user.copy(passwords = passwords)
        )
    }
    fun withSettings(settings: List<UserSettingViewDto>): Authentication {
        return copy(
            user = user.copy(settings = settings)
        )
    }
}
