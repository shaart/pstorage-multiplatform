package com.github.shaart.pstorage.multiplatform.dto

import com.github.shaart.pstorage.multiplatform.model.Authentication

data class PasswordViewDto(
    val alias: String,
    val copyValue: (authentication: Authentication) -> Unit,
) {
    fun copyPasswordCommand(authentication: Authentication): () -> Unit {
        return { copyValue(authentication) }
    }
}