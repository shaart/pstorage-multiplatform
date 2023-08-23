package com.github.shaart.pstorage.multiplatform.ui.view

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.shaart.pstorage.multiplatform.dto.PasswordViewDto
import com.github.shaart.pstorage.multiplatform.exception.GlobalExceptionHandler
import com.github.shaart.pstorage.multiplatform.model.Authentication
import com.github.shaart.pstorage.multiplatform.preview.PreviewData
import com.github.shaart.pstorage.multiplatform.service.password.PasswordService
import com.github.shaart.pstorage.multiplatform.ui.component.password.PasswordsTable
import com.github.shaart.pstorage.multiplatform.ui.component.password.AddPasswordRow

@Composable
fun MainView(
    authentication: Authentication,
    passwordService: PasswordService,
    onPasswordsChange: (List<PasswordViewDto>) -> Unit,
    globalExceptionHandler: GlobalExceptionHandler,
) {
    MaterialTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            PasswordsTable(
                authentication = authentication,
                modifier = Modifier.fillMaxWidth().fillMaxHeight().weight(.8f),
                globalExceptionHandler = globalExceptionHandler,
                onPasswordCopy = {
                    val copyCommand: () -> Unit = it.createCopyPasswordCommand(authentication = authentication)
                    copyCommand()
                },
                onPasswordDelete = {
                    passwordService.deletePassword(it.alias, authentication)
                    onPasswordsChange(authentication.user.passwords.minus(it))
                },
                onPasswordEdit = { password, newPasswordValue ->
                    val updatedPassword =
                        passwordService.updatePasswordValue(password, newPasswordValue, authentication)
                    onPasswordsChange(
                        authentication.user.passwords.stream()
                            .filter { it.alias != password.alias }
                            .map { it.copy() }
                            .toList()
                            .plus(updatedPassword)
                    )
                },
                onAliasEdit = { password, newAliasValue ->
                    val updatedPassword =
                        passwordService.updateAlias(password, newAliasValue, authentication)
                    onPasswordsChange(
                        authentication.user.passwords.stream()
                            .filter { it.alias != password.alias }
                            .map { it.copy() }
                            .toList()
                            .plus(updatedPassword)
                    )
                },
            )
            AddPasswordRow(
                modifier = Modifier.fillMaxWidth()
                    .padding(8.dp)
                    .height(70.dp),
                onAddNewPassword = { alias, rawPassword ->
                    val createdPassword =
                        passwordService.createPassword(authentication, alias, rawPassword)
                    onPasswordsChange(authentication.user.passwords.plus(createdPassword))
                },
                globalExceptionHandler = globalExceptionHandler,
            )
        }
    }
}

@Preview
@Composable
fun previewMainView() {
    MainView(
        authentication = PreviewData.previewAuthentication(100),
        passwordService = PreviewData.previewPasswordService(),
        onPasswordsChange = {},
        globalExceptionHandler = PreviewData.previewGlobalExceptionHandler()
    )
}