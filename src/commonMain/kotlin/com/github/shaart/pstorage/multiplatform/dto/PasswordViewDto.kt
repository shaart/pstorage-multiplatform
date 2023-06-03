package com.github.shaart.pstorage.multiplatform.dto

data class PasswordViewDto(
    val alias: String,
    val copyValue: () -> Unit,
)
