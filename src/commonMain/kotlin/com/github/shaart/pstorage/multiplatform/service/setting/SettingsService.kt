package com.github.shaart.pstorage.multiplatform.service.setting

import com.github.shaart.pstorage.multiplatform.dto.UserSettingViewDto
import com.github.shaart.pstorage.multiplatform.model.Authentication

interface SettingsService {
    fun saveSetting(aSetting: UserSettingViewDto, authentication: Authentication): UserSettingViewDto
}