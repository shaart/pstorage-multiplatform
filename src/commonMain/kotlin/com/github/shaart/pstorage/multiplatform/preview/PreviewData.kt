package com.github.shaart.pstorage.multiplatform.preview

import com.github.shaart.pstorage.multiplatform.config.ApplicationContext
import com.github.shaart.pstorage.multiplatform.config.PstorageProperties
import com.github.shaart.pstorage.multiplatform.dto.PasswordViewDto
import com.github.shaart.pstorage.multiplatform.dto.RoleViewDto
import com.github.shaart.pstorage.multiplatform.dto.UserViewDto
import com.github.shaart.pstorage.multiplatform.entity.EncryptionType
import com.github.shaart.pstorage.multiplatform.exception.GlobalExceptionHandler
import com.github.shaart.pstorage.multiplatform.model.Authentication
import com.github.shaart.pstorage.multiplatform.model.LoginModel
import com.github.shaart.pstorage.multiplatform.model.RegisterModel
import com.github.shaart.pstorage.multiplatform.service.auth.AuthService
import com.github.shaart.pstorage.multiplatform.service.password.PasswordService
import com.github.shaart.pstorage.multiplatform.util.ClipboardUtil
import java.time.LocalDateTime
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
        )

        fun previewPasswords(passwordsCount: Long = 100): MutableList<PasswordViewDto> =
            IntStream.iterate(1) { it + 1 }
                .limit(passwordsCount)
                .mapToObj { number -> previewPassword("alias$number", "$number") }
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

        fun previewGlobalExceptionHandler() =
            GlobalExceptionHandler(previewPstorageProperties())

    }
}