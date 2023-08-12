package com.github.shaart.pstorage.multiplatform.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.input.key.*
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.shaart.pstorage.multiplatform.config.ApplicationContext
import com.github.shaart.pstorage.multiplatform.dto.UserViewDto
import com.github.shaart.pstorage.multiplatform.model.LoginModel
import com.github.shaart.pstorage.multiplatform.model.RegisterModel
import com.github.shaart.pstorage.multiplatform.preview.PreviewData

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RegisterView(
    appContext: ApplicationContext,
    onRegisterSuccess: (UserViewDto) -> Unit,
    activeViewContext: ActiveViewContext,
) {
    val authService = appContext.authService()
    val globalExceptionHandler = appContext.globalExceptionHandler()

    MaterialTheme {
        var login by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var isPasswordHidden by remember { mutableStateOf(true) }
        val focusManager = LocalFocusManager.current

        val signUpButtonInteractionSource = remember { MutableInteractionSource() }
        val isSignUpFocused by signUpButtonInteractionSource.collectIsFocusedAsState()
        val signUpButtonColor =
            if (isSignUpFocused) MaterialTheme.colors.secondary
            else MaterialTheme.colors.primary

        val logInFunction: () -> Unit = globalExceptionHandler.runSafely {
            val user = authService.login(LoginModel(login, password))
            onRegisterSuccess(user)
        }

        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)
                .onKeyEvent {
                    if ((it.key == Key.Tab) && it.type == KeyEventType.KeyDown) {
                        if (it.isShiftPressed) {
                            focusManager.moveFocus(FocusDirection.Previous)
                        } else {
                            focusManager.moveFocus(FocusDirection.Next)
                        }
                        return@onKeyEvent true
                    }
                    false
                },
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
                label = { Text("Login") },
                onValueChange = { login = it },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
                    .onKeyEvent {
                        if ((it.key == Key.Enter) && it.type == KeyEventType.KeyDown) {
                            logInFunction()
                            return@onKeyEvent true
                        }
                        false
                    },
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                label = { Text("Password") },
                onValueChange = { password = it },
                visualTransformation = if (isPasswordHidden) PasswordVisualTransformation() else VisualTransformation.None,
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
                    .onKeyEvent {
                        if ((it.key == Key.Enter) && it.type == KeyEventType.KeyDown) {
                            logInFunction()
                            return@onKeyEvent true
                        }
                        false
                    },
                trailingIcon = {
                    val icon =
                        if (isPasswordHidden) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
                    val description = if (isPasswordHidden) "Show password" else "Hide password"

                    IconButton(onClick = { isPasswordHidden = !isPasswordHidden }) {
                        Icon(imageVector = icon, contentDescription = description)
                    }
                },
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = globalExceptionHandler.runSafely {
                    val createdUser = authService.register(RegisterModel(login, password))
                    onRegisterSuccess(createdUser)
                },
                interactionSource = signUpButtonInteractionSource,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = signUpButtonColor
                )
            ) {
                Text("Sign up")
            }
        }
    }
}

@Preview
@Composable
fun previewRegisterView() {
    RegisterView(
        appContext = PreviewData.previewApplicationContext(),
        onRegisterSuccess = {},
        activeViewContext = PreviewData.previewActiveViewContextUnauthorized(),
    )
}