package com.github.shaart.pstorage.multiplatform.ui.component.password

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.*
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun ToggleablePasswordField(
    label: String = "Password",
    passwordValue: String,
    onValueChange: (String) -> Unit,
    onEnterPressed: () -> Unit,
    singleLine: Boolean = true,
    enabled: Boolean = true,
    modifier: Modifier = Modifier.fillMaxWidth(),
) {
    var isPasswordHidden by remember { mutableStateOf(true) }

    OutlinedTextField(
        enabled = enabled,
        label = { Text(label) },
        value = passwordValue,
        onValueChange = onValueChange,
        visualTransformation = getPasswordTransformation(isPasswordHidden),
        singleLine = singleLine,
        modifier = modifier.onKeyEvent { callIfEnterPressed(it, onEnterPressed) },
        trailingIcon = {
            getPasswordIcon(
                isPasswordHidden = isPasswordHidden,
                toggleVisibility = { isPasswordHidden = it }
            )
        },
    )
}

@Composable
fun getPasswordIcon(isPasswordHidden: Boolean, toggleVisibility: (Boolean) -> Unit) {
    val icon = if (isPasswordHidden) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
    val description = if (isPasswordHidden) "Show password" else "Hide password"

    return IconButton(onClick = { toggleVisibility(!isPasswordHidden) }) {
        Icon(imageVector = icon, contentDescription = description)
    }
}

fun getPasswordTransformation(isPasswordHidden: Boolean): VisualTransformation =
    if (isPasswordHidden) PasswordVisualTransformation() else VisualTransformation.None

@OptIn(ExperimentalComposeUiApi::class)
private fun callIfEnterPressed(it: KeyEvent, onEnterPressed: () -> Unit): Boolean {
    if ((it.key == Key.Enter) && it.type == KeyEventType.KeyDown) {
        onEnterPressed()
        return true
    }
    return false
}

@Preview
@Composable
fun previewToggleablePasswordField() {
    var password by remember { mutableStateOf("") }
    ToggleablePasswordField(
        label = "A password",
        passwordValue = password,
        onValueChange = { password = it },
        onEnterPressed = {},
    )
}