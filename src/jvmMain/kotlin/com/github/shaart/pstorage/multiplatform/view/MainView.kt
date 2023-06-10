package com.github.shaart.pstorage.multiplatform.view

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.shaart.pstorage.multiplatform.model.Authentication
import com.github.shaart.pstorage.multiplatform.preview.PreviewData
import com.github.shaart.pstorage.multiplatform.ui.PasswordsTable
import com.github.shaart.pstorage.multiplatform.ui.password.AddPasswordRow

@Composable
@Preview
fun MainView(
    authentication: Authentication,
) {
    MaterialTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            PasswordsTable(
                authentication = authentication,
                modifier = Modifier.fillMaxWidth().fillMaxHeight().weight(.8f)
            )
            AddPasswordRow(
                modifier = Modifier.fillMaxWidth()
                    .padding(8.dp)
                    .height(70.dp),
            )
        }
    }
}

@Preview
@Composable
fun previewMainView() {
    MainView(
        authentication = PreviewData.previewAuthentication(100)
    )
}