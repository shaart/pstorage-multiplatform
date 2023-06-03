package com.github.shaart.pstorage.multiplatform.dto

import java.time.LocalDateTime

data class RoleDto(
    var id: String,
    val name: String,
    val createdAt: LocalDateTime,
)