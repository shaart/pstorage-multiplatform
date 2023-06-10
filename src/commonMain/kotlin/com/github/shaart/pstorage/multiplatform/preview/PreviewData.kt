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
import com.github.shaart.pstorage.multiplatform.util.ClipboardUtil
import java.time.LocalDateTime
import java.util.stream.IntStream

class PreviewData {

    companion object {
        fun previewAuthentication(passwordsCount: Long = 100) = Authentication(
            user = previewUserViewDto(passwordsCount),
        )

        private fun previewUserViewDto(passwordsCount: Long = 100) = UserViewDto(
            id = "1",
            name = "artur",
            masterPassword = "pwd",
            encryptionType = EncryptionType.AES_CODER,
            role = RoleViewDto("1", "USER", LocalDateTime.now()),
            createdAt = LocalDateTime.now(),
            passwords = previewPasswords(passwordsCount),
        )

        private fun previewPasswords(passwordsCount: Long = 100): List<PasswordViewDto> =
            IntStream.iterate(1) { it + 1 }
                .limit(passwordsCount)
                .mapToObj { number ->
                    PasswordViewDto(
                        alias = "alias$number",
                        copyValue = { ClipboardUtil.setValueToClipboard("$number") }
                    )
                }.toList()

        fun previewApplicationContext(): ApplicationContext {
            return object : ApplicationContext {
                override fun authService(): AuthService {
                    return object : AuthService {
                        override fun register(registerModel: RegisterModel): UserViewDto {
                            return previewUserViewDto(100)
                        }

                        override fun login(loginModel: LoginModel): UserViewDto {
                            return previewUserViewDto(100)
                        }

                    }
                }

                override fun properties(): PstorageProperties {
                    return PstorageProperties()
                }

                override fun globalExceptionHandler(): GlobalExceptionHandler {
                    return GlobalExceptionHandler(properties())
                }

            }
        }
    }
}