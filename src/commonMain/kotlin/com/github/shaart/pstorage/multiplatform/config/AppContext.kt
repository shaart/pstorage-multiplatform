package com.github.shaart.pstorage.multiplatform.config

import com.github.shaart.pstorage.multiplatform.db.PstorageDatabase
import com.github.shaart.pstorage.multiplatform.exception.GlobalExceptionHandler
import com.github.shaart.pstorage.multiplatform.service.auth.AuthService

data class AppContext(
    val authService: AuthService,
    val database: PstorageDatabase,
    val properties: PstorageProperties,
    val globalExceptionHandler: GlobalExceptionHandler,
)