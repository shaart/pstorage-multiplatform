package com.github.shaart.pstorage.multiplatform.view

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.shaart.pstorage.multiplatform.config.AppContext
import com.github.shaart.pstorage.multiplatform.exception.GlobalExceptionHandler.Companion.runSafely
import com.github.shaart.pstorage.multiplatform.model.LoginModel
import com.github.shaart.pstorage.multiplatform.model.RegisterModel
import migrations.Usr_users

@Composable
@Preview
fun AuthView(
    appContext: AppContext,
    onAuthSuccess: (Usr_users) -> Unit
) {
    val authService = appContext.authService

    MaterialTheme {
        var login by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }

        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Authorization",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp)
            )
            OutlinedTextField(
                value = login,
                label = { Text("Login") },
                onValueChange = { login = it },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                label = { Text("Password") },
                onValueChange = { password = it },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                onClick = runSafely {
                    val user = authService.login(LoginModel(login, password))
                    onAuthSuccess(user)
                }
            ) {
                Text("Sign in")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = runSafely {
                    val createdUser = authService.register(RegisterModel(login, password))
                    onAuthSuccess(createdUser)
                }
            ) {
                Text("Sign up")
            }
        }
    }
}


