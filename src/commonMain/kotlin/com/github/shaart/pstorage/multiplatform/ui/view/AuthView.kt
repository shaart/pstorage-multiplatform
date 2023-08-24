package com.github.shaart.pstorage.multiplatform.ui.view

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.shaart.pstorage.multiplatform.config.ApplicationContext
import com.github.shaart.pstorage.multiplatform.dto.UserViewDto
import com.github.shaart.pstorage.multiplatform.model.LoginModel
import com.github.shaart.pstorage.multiplatform.preview.PreviewData
import com.github.shaart.pstorage.multiplatform.ui.component.password.ToggleablePasswordField

@Composable
fun AuthView(
    appContext: ApplicationContext,
    onAuthSuccess: (UserViewDto) -> Unit,
    onRegisterClick: () -> Unit
) {
    val authService = appContext.authService()
    val globalExceptionHandler = appContext.globalExceptionHandler()

    MaterialTheme {
        var login by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }

        val onLogInClick: () -> Unit = globalExceptionHandler.runSafely {
            val user = authService.login(LoginModel(login, password))
            onAuthSuccess(user)
        }

        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = "Authorization",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Divider(modifier = Modifier.padding(top = 8.dp, bottom = 32.dp))
            OutlinedTextField(
                value = login,
                label = { Text("Username") },
                onValueChange = { login = it },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
                    .onKeyEvent { callIfEnterPressed(it, onLogInClick) },
            )

            Spacer(modifier = Modifier.height(16.dp))

            ToggleablePasswordField(
                label = "Password",
                passwordValue = password,
                onValueChange = { password = it },
                onEnterPressed = onLogInClick,
            )
            Button(
                modifier = Modifier.fillMaxWidth()
                    .onKeyEvent { callIfEnterPressed(it, onLogInClick) },
                onClick = onLogInClick,
            ) {
                Text("Sign in")
            }

            TextButton(
                modifier = Modifier.fillMaxWidth()
                    .onKeyEvent { callIfEnterPressed(it, onRegisterClick) },
                onClick = onRegisterClick,
            ) {
                Text(
                    text = "Create a new account",
                    textDecoration = TextDecoration.Underline
                )
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
private fun callIfEnterPressed(it: KeyEvent, onClick: () -> Unit): Boolean {
    if ((it.key == Key.Enter) && it.type == KeyEventType.KeyDown) {
        onClick()
        return true
    }
    return false
}

@Preview
@Composable
fun previewAuthView() {
    AuthView(
        appContext = PreviewData.previewApplicationContext(),
        onAuthSuccess = {},
        onRegisterClick = {}
    )
}