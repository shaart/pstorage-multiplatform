package com.github.shaart.pstorage.multiplatform.dto

import com.github.shaart.pstorage.multiplatform.enums.SettingType
import java.time.LocalDateTime

data class UserSettingViewDto(
    val id: String? = null,
    val name: String,
    var value: String,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null,
    var isUserDefined: Boolean,
    val settingType: SettingType,
) {

    fun changeValue(newValue: String) {
        value = newValue
        isUserDefined = true
    }

    override fun toString(): String {
        return "UserSetting(id='$id', name='$name', value='$value', isUserDefined='$isUserDefined')"
    }
}
