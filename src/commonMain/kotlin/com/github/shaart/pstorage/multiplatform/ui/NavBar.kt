package com.github.shaart.pstorage.multiplatform.ui

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@Composable
fun NavBar(
    activeViewContext: ActiveViewContext,
    content: @Composable (() -> Unit)
) {
    val goBackButtonInteractionSource = remember { MutableInteractionSource() }
    val isGoBackFocused by goBackButtonInteractionSource.collectIsFocusedAsState()
    val goBackButtonColor =
        if (isGoBackFocused) MaterialTheme.colors.secondary
        else MaterialTheme.colors.primary

    Column {
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { activeViewContext.goBack() },
            interactionSource = goBackButtonInteractionSource,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = goBackButtonColor
            )
        ) {
            Text("Back")
        }
        content()
    }
}