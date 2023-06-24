package com.github.shaart.pstorage.multiplatform.service.mapper

import com.github.shaart.pstorage.multiplatform.dto.UserSettingViewDto
import com.github.shaart.pstorage.multiplatform.enums.AppSettings
import com.github.shaart.pstorage.multiplatform.enums.SettingType
import migrations.Usr_settings
import java.time.LocalDateTime
import java.util.*
import java.util.stream.Stream

class UserSettingsMapper {
    fun entityToViewDtoWithDefaultSettings(settings: List<Usr_settings>): List<UserSettingViewDto> {
        val userSettings = settings.map { entityToViewDto(it) }
        return withDefaultSettings(userSettings)
    }

    fun entityToViewDto(setting: Usr_settings): UserSettingViewDto {
        return UserSettingViewDto(
            id = setting.id.toString(),
            name = setting.name,
            value = setting.value_,
            createdAt = LocalDateTime.parse(setting.created_at),
            updatedAt = LocalDateTime.parse(setting.updated_at),
            isUserDefined = true,
            settingType = SettingType.valueOf(setting.setting_type),
        )
    }

    private fun withDefaultSettings(userSettings: List<UserSettingViewDto>): List<UserSettingViewDto> {
        val userSettingNames = userSettings.map { it.name }
        val notDefinedSettings = Arrays.stream(AppSettings.values())
            .filter { !userSettingNames.contains(it.settingName) }
            .map {
                UserSettingViewDto(
                    name = it.settingName,
                    value = it.defaultValue,
                    isUserDefined = false,
                    settingType = it.settingType,
                )
            }
            .toList()

        return Stream.of(userSettings, notDefinedSettings)
            .flatMap { it.stream() }
            .toList()
    }
}
