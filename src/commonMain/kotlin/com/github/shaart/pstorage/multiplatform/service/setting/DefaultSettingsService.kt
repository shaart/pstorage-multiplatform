package com.github.shaart.pstorage.multiplatform.service.setting

import com.github.shaart.pstorage.multiplatform.db.UserSettingQueries
import com.github.shaart.pstorage.multiplatform.dto.UserSettingViewDto
import com.github.shaart.pstorage.multiplatform.exception.AppException
import com.github.shaart.pstorage.multiplatform.logger
import com.github.shaart.pstorage.multiplatform.model.Authentication
import com.github.shaart.pstorage.multiplatform.service.mapper.UserSettingsMapper

class DefaultSettingsService(
    val settingQueries: UserSettingQueries,
    val settingsMapper: UserSettingsMapper,
) : SettingsService {

    private val log = logger()

    override fun saveSetting(
        aSetting: UserSettingViewDto, authentication: Authentication
    ): UserSettingViewDto {
        log.info("Saving setting with name = '{}'", aSetting.name)

        val isExistsInDatabase = settingQueries.existsByNameAndUserId(
            name = aSetting.name,
            userId = authentication.user.id.toLong()
        ).executeAsOne()

        if (isExistsInDatabase) {
            return updateUserSetting(authentication, aSetting)
        }

        return createNewUserSetting(authentication, aSetting)
    }

    private fun updateUserSetting(
        authentication: Authentication,
        aSetting: UserSettingViewDto
    ): UserSettingViewDto {
        log.debug("Updating an existing user's setting, id = {}", aSetting.id)
        val settingName = aSetting.name
        val affectedCount = settingQueries.transactionWithResult {
            settingQueries.updateSettingByUserIdAndName(
                userId = authentication.user.id.toLong(),
                name = settingName,
                value = aSetting.value,
            )
            settingQueries.countAffectedRows().executeAsOne()
        }
        if (affectedCount == 0L) {
            throw AppException(
                "Cannot update user's setting with name = '$settingName' because " +
                        "it's not found for current user"
            )
        }
        return aSetting.also {
            log.info("Successfully updated user's setting with name = '{}'", settingName)
        }
    }

    private fun createNewUserSetting(
        authentication: Authentication,
        aSetting: UserSettingViewDto
    ): UserSettingViewDto {
        log.debug("Creating a new user's setting because it doesn't exists")
        val savedSetting = settingQueries.transactionWithResult {
            settingQueries.createSetting(
                userId = authentication.user.id.toLong(),
                name = aSetting.name,
                value = aSetting.value,
                settingType = aSetting.settingType.name
            )
            val lastInsertRowId = settingQueries.lastInsertRowId().executeAsOne()
            settingQueries.findById(id = lastInsertRowId).executeAsOne()
        }
        return settingsMapper.entityToViewDto(savedSetting).also {
            log.info("Successfully created setting with name = '{}'", it.name)
        }
    }
}