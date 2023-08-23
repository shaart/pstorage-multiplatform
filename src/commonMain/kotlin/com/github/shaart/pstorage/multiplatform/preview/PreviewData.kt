package com.github.shaart.pstorage.multiplatform.preview

import com.github.shaart.pstorage.multiplatform.config.ApplicationContext
import com.github.shaart.pstorage.multiplatform.config.PstorageProperties
import com.github.shaart.pstorage.multiplatform.dto.PasswordViewDto
import com.github.shaart.pstorage.multiplatform.dto.RoleViewDto
import com.github.shaart.pstorage.multiplatform.dto.UserSettingViewDto
import com.github.shaart.pstorage.multiplatform.dto.UserViewDto
import com.github.shaart.pstorage.multiplatform.enums.AppSettings
import com.github.shaart.pstorage.multiplatform.enums.EncryptionType
import com.github.shaart.pstorage.multiplatform.exception.GlobalExceptionHandler
import com.github.shaart.pstorage.multiplatform.model.Authentication
import com.github.shaart.pstorage.multiplatform.model.LoginModel
import com.github.shaart.pstorage.multiplatform.model.RegisterModel
import com.github.shaart.pstorage.multiplatform.service.auth.AuthService
import com.github.shaart.pstorage.multiplatform.service.password.PasswordService
import com.github.shaart.pstorage.multiplatform.service.setting.SettingsService
import com.github.shaart.pstorage.multiplatform.ui.model.navigation.ActiveViewContext
import com.github.shaart.pstorage.multiplatform.util.ClipboardUtil
import java.time.LocalDateTime
import java.util.*
import java.util.concurrent.ThreadLocalRandom
import java.util.stream.IntStream

@Suppress("MemberVisibilityCanBePrivate")
class PreviewData {

    companion object {
        fun previewAuthentication(passwordsCount: Long = 100) = Authentication(
            user = previewUserViewDto(passwordsCount),
        )

        fun previewUserViewDto(passwordsCount: Long = 100) = UserViewDto(
            id = "1",
            name = "artur",
            masterPassword = "pwd",
            encryptionType = EncryptionType.AES_CODER,
            role = RoleViewDto("1", "USER", LocalDateTime.now()),
            createdAt = LocalDateTime.now(),
            passwords = previewPasswords(passwordsCount),
            settings = previewAllDefaultSettings()
        )

        private fun previewAllDefaultSettings(): List<UserSettingViewDto> =
            Arrays.stream(AppSettings.values())
                .map {
                    UserSettingViewDto(
                        name = it.settingName,
                        value = it.defaultValue,
                        isUserDefined = false,
                        settingType = it.settingType,
                    )
                }
                .toList()

        fun previewPasswords(passwordsCount: Long = 100): MutableList<PasswordViewDto> =
            IntStream.iterate(1) { it + 1 }
                .limit(passwordsCount)
                .mapToObj { number ->
                    val repeatCount = ThreadLocalRandom.current().nextInt(0, 12)
                    var veryRepeated = "very ".repeat(repeatCount)
                    if (repeatCount > 0) {
                        veryRepeated += "long "
                    }
                    val alias = "${veryRepeated}alias #$number"
                    return@mapToObj previewPassword(alias = alias, value = "$number")
                }
                .toList()

        fun previewPassword(alias: String, value: String) = PasswordViewDto(
            alias = alias,
            copyValue = { ClipboardUtil.setValueToClipboard(value) }
        )

        fun previewApplicationContext(): ApplicationContext {
            return object : ApplicationContext {
                override fun authService(): AuthService {
                    return previewAuthService()
                }

                override fun settingsService(): SettingsService {
                    return previewSettingsService()
                }

                override fun properties(): PstorageProperties {
                    return previewPstorageProperties()
                }

                override fun globalExceptionHandler(): GlobalExceptionHandler {
                    return previewGlobalExceptionHandler()
                }

                override fun passwordService(): PasswordService {
                    return previewPasswordService()
                }

            }
        }

        fun previewPasswordService() = object : PasswordService {
            override fun createPassword(
                authentication: Authentication,
                alias: String,
                rawPassword: String
            ): PasswordViewDto {
                return previewPassword(alias, rawPassword)
            }

            override fun deletePassword(alias: String, authentication: Authentication) {
                // do nothing
            }

            override fun updatePasswordValue(
                password: PasswordViewDto,
                newPasswordValue: String,
                authentication: Authentication
            ): PasswordViewDto {
                return password
            }

            override fun updateAlias(
                password: PasswordViewDto,
                newAliasValue: String,
                authentication: Authentication
            ): PasswordViewDto {
                return password.copy(alias = newAliasValue)
            }
        }

        fun previewPstorageProperties() = PstorageProperties()

        fun previewAuthService() = object : AuthService {
            override fun register(registerModel: RegisterModel): UserViewDto {
                return previewUserViewDto(100)
            }

            override fun login(loginModel: LoginModel): UserViewDto {
                return previewUserViewDto(100)
            }
        }

        fun previewSettingsService() = object : SettingsService {
            override fun saveSetting(
                aSetting: UserSettingViewDto,
                authentication: Authentication
            ): UserSettingViewDto {
                return aSetting
            }
        }

        fun previewGlobalExceptionHandler() =
            GlobalExceptionHandler(previewPstorageProperties())

        fun previewActiveViewContextUnauthorized(): ActiveViewContext {
            return previewActiveViewContext(authentication = null)
        }

        fun previewActiveViewContext(authentication: Authentication? = null): ActiveViewContext {
            return ActiveViewContext(
                getAuthentication = { authentication },
                setAuthentication = {},
                changeView = {},
                goBack = {},
                canGoBack = { false },
                clearHistory = {},
            )
        }
    }
}