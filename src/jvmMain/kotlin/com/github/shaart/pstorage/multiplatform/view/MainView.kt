package com.github.shaart.pstorage.multiplatform.view

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.github.shaart.pstorage.multiplatform.config.AppContext
import com.github.shaart.pstorage.multiplatform.model.Authentication

@Composable
@Preview
fun MainView(
    appContext: AppContext,
    authentication: Authentication
) {
    val text by remember { mutableStateOf("Hello, ${authentication.user.name}!") }

    MaterialTheme {
        Button(
            onClick = {}
        ) {
            Text(text)
        }
    }
}