package com.github.shaart.pstorage.multiplatform.ui.view

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.shaart.pstorage.multiplatform.config.ApplicationContext
import com.github.shaart.pstorage.multiplatform.dto.UserViewDto
import com.github.shaart.pstorage.multiplatform.model.RegisterModel
import com.github.shaart.pstorage.multiplatform.preview.PreviewData
import com.github.shaart.pstorage.multiplatform.ui.component.password.ToggleablePasswordField

@Composable
fun RegisterView(
    appContext: ApplicationContext,
    onRegisterSuccess: (UserViewDto) -> Unit,
) {
    val authService = appContext.authService()
    val globalExceptionHandler = appContext.globalExceptionHandler()

    MaterialTheme {
        var login by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var confirmationPassword by remember { mutableStateOf("") }

        val onSignUpClick: () -> Unit = globalExceptionHandler.runSafely {
            val createdUser = authService.register(
                RegisterModel(
                    login = login,
                    password = password,
                    confirmationPassword = confirmationPassword
                )
            )
            onRegisterSuccess(createdUser)
        }

        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = "Registration",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp)
            )
            OutlinedTextField(
                value = login,
                label = { Text("Username") },
                onValueChange = { login = it },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
                    .onKeyEvent { signUpIfEnterPressed(it, onSignUpClick) },
            )

            Spacer(modifier = Modifier.height(16.dp))

            ToggleablePasswordField(
                label = "Password",
                passwordValue = password,
                onValueChange = { password = it },
                onEnterPressed = onSignUpClick,
            )

            Spacer(modifier = Modifier.height(16.dp))

            ToggleablePasswordField(
                label = "Confirmation password",
                passwordValue = confirmationPassword,
                onValueChange = { confirmationPassword = it },
                onEnterPressed = onSignUpClick,
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = globalExceptionHandler.runSafely {
                    val createdUser = authService.register(RegisterModel(login, password, confirmationPassword))
                    onRegisterSuccess(createdUser)
                },
            ) {
                Text("Create an account")
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
private fun signUpIfEnterPressed(it: KeyEvent, signUpFunction: () -> Unit): Boolean {
    if ((it.key == Key.Enter) && it.type == KeyEventType.KeyDown) {
        signUpFunction()
        return true
    }
    return false
}

@Preview
@Composable
fun previewRegisterView() {
    RegisterView(
        appContext = PreviewData.previewApplicationContext(),
        onRegisterSuccess = {},
    )
}