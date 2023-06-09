package com.github.shaart.pstorage.multiplatform.service.mapper

import com.github.shaart.pstorage.multiplatform.dto.RoleViewDto
import migrations.Dct_roles
import java.time.LocalDateTime

class RoleMapper {
    fun entityToViewDto(role: Dct_roles): RoleViewDto {
        return RoleViewDto(
            id = role.id.toString(),
            name = role.name,
            createdAt = LocalDateTime.parse(role.created_at)
        )
    }
}
