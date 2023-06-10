package com.github.shaart.pstorage.multiplatform.ui.password

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.github.shaart.pstorage.multiplatform.exception.GlobalExceptionHandler
import com.github.shaart.pstorage.multiplatform.preview.PreviewData

@Composable
@Preview
fun AddPasswordRow(
    modifier: Modifier = Modifier.fillMaxWidth()
        .padding(8.dp)
        .height(70.dp),
    onAddNewPassword: (alias: String, rawPassword: String) -> Unit,
    globalExceptionHandler: GlobalExceptionHandler,
) {
    MaterialTheme {
        var alias by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }

        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.Center,
        ) {
            TextField(
                placeholder = {
                    Text("Alias")
                },
                value = alias,
                onValueChange = { alias = it },
                singleLine = true,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .weight(.35f),
            )
            TextField(
                placeholder = { Text("Password") },
                value = password,
                onValueChange = { password = it },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .weight(.35f),
            )
            Button(
                onClick = globalExceptionHandler.runSafely {
                    onAddNewPassword(alias, password)
                    alias = ""
                    password = ""
                },
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .weight(weight = .3f, fill = false),
            ) {
                Text(
                    text = "Add",
                )
            }
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