package com.github.shaart.pstorage.multiplatform.ui.component.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.shaart.pstorage.multiplatform.ui.model.navigation.ActiveViewContext

@Composable
fun NavBar(
    activeViewContext: ActiveViewContext,
    content: @Composable (() -> Unit)
) {

    Column {
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { activeViewContext.goBack() },
        ) {
            Text("Back")
        }
        content()
    }
}