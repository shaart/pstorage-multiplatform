package com.github.shaart.pstorage.multiplatform.config

import com.github.shaart.pstorage.multiplatform.db.PstorageDatabase
import com.github.shaart.pstorage.multiplatform.service.AuthService

data class AppContext(
    val authService: AuthService,
    val database: PstorageDatabase,
)