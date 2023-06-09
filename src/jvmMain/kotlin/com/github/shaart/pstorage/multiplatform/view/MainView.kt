package com.github.shaart.pstorage.multiplatform.view

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import com.github.shaart.pstorage.multiplatform.dto.PasswordViewDto
import com.github.shaart.pstorage.multiplatform.dto.RoleViewDto
import com.github.shaart.pstorage.multiplatform.dto.UserViewDto
import com.github.shaart.pstorage.multiplatform.entity.EncryptionType
import com.github.shaart.pstorage.multiplatform.model.Authentication
import com.github.shaart.pstorage.multiplatform.ui.PasswordsTable
import com.github.shaart.pstorage.multiplatform.util.ClipboardUtil
import java.time.LocalDateTime

@Composable
@Preview
fun MainView(
    authentication: Authentication,
) {
    MaterialTheme {
        PasswordsTable(
            passwords = authentication.user.passwords
        )
    }
}

@Preview
@Composable
fun preview() {
    MainView(
        authentication = Authentication(
            user = UserViewDto(
                id = "1",
                name = "artur",
                masterPassword = "pwd",
                encryptionType = EncryptionType.AES_CODER,
                role = RoleViewDto("1", "USER", LocalDateTime.now()),
                LocalDateTime.now(),
                listOf(
                    PasswordViewDto(
                        alias = "123",
                        copyValue = { ClipboardUtil.setValueToClipboard("321") }
                    ),
                    PasswordViewDto(
                        alias = "456",
                        copyValue = { ClipboardUtil.setValueToClipboard("444") }
                    ),
                    PasswordViewDto(
                        alias = "789",
                        copyValue = { ClipboardUtil.setValueToClipboard("555") }
                    ),
                )
            )
        )
    )
}