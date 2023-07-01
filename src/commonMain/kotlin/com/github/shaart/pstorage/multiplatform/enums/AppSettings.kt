package com.github.shaart.pstorage.multiplatform.enums

import com.github.shaart.pstorage.multiplatform.dto.UserViewDto
import com.github.shaart.pstorage.multiplatform.exception.AppException
import java.util.*
import java.util.function.Function
import java.util.stream.Collectors

enum class AppSettings(
    val settingName: String,
    val settingDescription: String,
    val settingType: SettingType,
    val defaultValue: String,
) {
    TRAY_NOTIFICATION_ON_CLOSE(
        "tray.notification.on.close",
        "Notify about hiding to tray",
        SettingType.TOGGLE,
        "true"
    );

    fun isEnabled(user: UserViewDto?): String {
        if (user == null) {
            return defaultValue
        }
        return user.settings.stream()
            .filter { it.name == settingName }
            .map { it.value }
            .findFirst()
            .orElse(defaultValue)
    }

    companion object {

        private val settingNamesToSettings: Map<String, AppSettings> =
            Arrays.stream(AppSettings.values())
                .collect(Collectors.toMap(AppSettings::settingName, Function.identity()))

        fun getBySettingName(settingName: String): AppSettings {
            return settingNamesToSettings[settingName]
                ?: throw AppException("Unknown setting: '%s'".format(settingName))
        }
    }
}