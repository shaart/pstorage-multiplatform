package com.github.shaart.pstorage.multiplatform.service.password

import com.github.shaart.pstorage.multiplatform.dto.PasswordViewDto
import com.github.shaart.pstorage.multiplatform.model.Authentication

interface PasswordService {
    fun createPassword(authentication: Authentication, alias: String, rawPassword: String): PasswordViewDto

}

