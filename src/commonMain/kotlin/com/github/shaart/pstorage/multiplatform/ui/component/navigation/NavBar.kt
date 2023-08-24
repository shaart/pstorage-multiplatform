package com.github.shaart.pstorage.multiplatform.ui.component.navigation

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.shaart.pstorage.multiplatform.preview.PreviewData
import com.github.shaart.pstorage.multiplatform.ui.model.navigation.ActiveViewContext
import com.github.shaart.pstorage.multiplatform.ui.model.navigation.ViewContextSnapshot
import com.github.shaart.pstorage.multiplatform.ui.model.navigation.Views

@Composable
fun NavBar(
    activeViewContext: ActiveViewContext,
    content: @Composable (() -> Unit)
) {
    val sideBarButtonsModifier: Modifier by remember {
        derivedStateOf {
            Modifier.height(52.dp).width(52.dp)
                .padding(top = 8.dp)
        }
    }
    Row {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(70.dp)
                .fillMaxHeight()
                .background(color = MaterialTheme.colors.surface),
        ) {
            Button(
                modifier = sideBarButtonsModifier,
                colors = sideBarButtonsColors(),
                onClick = { activeViewContext.changeView(ViewContextSnapshot(view = Views.MAIN)) },
            ) {
                Icon(
                    imageVector = Icons.Filled.AccountBox,
                    contentDescription = "Main",
                )
            }
            Button(
                modifier = sideBarButtonsModifier,
                colors = sideBarButtonsColors(),
                onClick = { activeViewContext.changeView(ViewContextSnapshot(view = Views.SETTINGS)) },
            ) {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = "Settings",
                )
            }
        }
        Column {
            content()
        }
    }
}

@Composable
fun sideBarButtonsColors(): ButtonColors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.surface)

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