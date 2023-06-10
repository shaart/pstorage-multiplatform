package com.github.shaart.pstorage.multiplatform.config

import com.github.shaart.pstorage.multiplatform.exception.GlobalExceptionHandler
import com.github.shaart.pstorage.multiplatform.service.auth.AuthService
import com.github.shaart.pstorage.multiplatform.service.auth.DefaultAuthService

interface ApplicationContext {
    fun authService(): AuthService
    fun properties(): PstorageProperties
    fun globalExceptionHandler(): GlobalExceptionHandler
}

data class DefaultApplicationContext(
    val authService: DefaultAuthService,
    val properties: PstorageProperties,
    val globalExceptionHandler: GlobalExceptionHandler,
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
}