package com.github.shaart.pstorage.multiplatform.config

import com.github.shaart.pstorage.multiplatform.exception.GlobalExceptionHandler
import com.github.shaart.pstorage.multiplatform.service.auth.AuthService
import com.github.shaart.pstorage.multiplatform.service.auth.DefaultAuthService
import com.github.shaart.pstorage.multiplatform.service.password.PasswordService

interface ApplicationContext {
    fun authService(): AuthService
    fun properties(): PstorageProperties
    fun globalExceptionHandler(): GlobalExceptionHandler
    fun passwordService(): PasswordService
}

data class DefaultApplicationContext(
    val authService: DefaultAuthService,
    val properties: PstorageProperties,
    val globalExceptionHandler: GlobalExceptionHandler,
    val passwordService: PasswordService,
) : ApplicationContext {
    override fun authService(): DefaultAuthService {
        return authService
    }

    override fun properties(): PstorageProperties {
        return properties
    }

    override fun globalExceptionHandler(): GlobalExceptionHandler {
        return globalExceptionHandler
    }

    override fun passwordService(): PasswordService {
        return passwordService
    }
}