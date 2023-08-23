package com.github.shaart.pstorage.multiplatform.ui.component.navigation

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.shaart.pstorage.multiplatform.preview.PreviewData
import com.github.shaart.pstorage.multiplatform.ui.model.navigation.ActiveViewContext

@Composable
fun NavBar(
    activeViewContext: ActiveViewContext,
    content: @Composable (() -> Unit)
) {

    Column {
        Box(modifier = Modifier.fillMaxHeight(fraction = 0.9f)) {
            content()
        }
        Button(
            enabled = activeViewContext.canGoBack(),
            modifier = Modifier.fillMaxWidth(),
            onClick = { activeViewContext.goBack() },
        ) {
            Text("Back")
        }
    }
}

@Preview
@Composable
fun previewNavBar() {
    NavBar(
        activeViewContext = PreviewData.previewActiveViewContextUnauthorized(),
    ) {
        TextField(value = "text", onValueChange = {})
        TextField(value = "another text", onValueChange = {})
        Button(onClick = {}) {
            Text(text = "Button")
        }
    }
}