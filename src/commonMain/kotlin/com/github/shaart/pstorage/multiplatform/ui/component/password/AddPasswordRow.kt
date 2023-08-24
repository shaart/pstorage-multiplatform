package com.github.shaart.pstorage.multiplatform.ui.component.password

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.*
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.github.shaart.pstorage.multiplatform.exception.GlobalExceptionHandler
import com.github.shaart.pstorage.multiplatform.preview.PreviewData

@OptIn(ExperimentalComposeUiApi::class)
@Composable
@Preview
fun AddPasswordRow(
    modifier: Modifier = Modifier.fillMaxWidth()
        .padding(8.dp)
        .height(70.dp),
    onAddNewPassword: (alias: String, rawPassword: String) -> Unit,
    globalExceptionHandler: GlobalExceptionHandler,
) {
    var alias by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val addNewPasswordFunction: () -> Unit = globalExceptionHandler.runSafely {
        onAddNewPassword(alias, password)
        alias = ""
        password = ""
    }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        OutlinedTextField(
            placeholder = { Text("Alias", style = MaterialTheme.typography.body1) },
            value = alias,
            onValueChange = { alias = it },
            singleLine = true,
            textStyle = MaterialTheme.typography.body1,
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 8.dp)
                .weight(.35f),
        )
        OutlinedTextField(
            placeholder = { Text("Password", style = MaterialTheme.typography.body1) },
            value = password,
            onValueChange = { password = it },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            textStyle = MaterialTheme.typography.body1,
            modifier = Modifier.fillMaxWidth()
                .weight(.35f)
                .padding(horizontal = 8.dp)
                .onKeyEvent {
                    if ((it.key == Key.Enter) && it.type == KeyEventType.KeyDown) {
                        addNewPasswordFunction()
                        return@onKeyEvent true
                    }
                    false
                },
        )
        Button(
            onClick = addNewPasswordFunction,
            modifier = Modifier.padding(horizontal = 8.dp)
                .fillMaxWidth()
                .fillMaxHeight()
                .weight(weight = .3f, fill = false),
        ) {
            Text(text = "Add")
        }
    }
}

@Preview
@Composable
fun previewAddPasswordRow() {
    AddPasswordRow(
        onAddNewPassword = { _, _ -> },
        globalExceptionHandler = PreviewData.previewGlobalExceptionHandler(),
    )
}