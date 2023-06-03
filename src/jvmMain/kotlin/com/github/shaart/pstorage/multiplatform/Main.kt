package com.github.shaart.pstorage.multiplatform

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import com.github.shaart.pstorage.multiplatform.config.AppConfig
import com.github.shaart.pstorage.multiplatform.config.AppContext
import com.github.shaart.pstorage.multiplatform.model.Authentication
import com.github.shaart.pstorage.multiplatform.view.AuthView
import com.github.shaart.pstorage.multiplatform.view.MainView
import kotlinx.coroutines.delay

const val APPLICATION_NAME = "PStorage"
val appContext: AppContext = AppConfig.init()

fun main() = application {
    var isApplicationLoading by remember { mutableStateOf(true) }
    var currentAuthentication: Authentication? by remember { mutableStateOf(null) }

    LaunchedEffect(Unit) {
        delay(1000) // TODO database migrations, resources loading
        isApplicationLoading = false
    }

    val trayState = rememberTrayState()
    Tray(
        icon = painterResource("assets/icons/tray/icon16.png"),
        menu = {
            Item("Quit App", onClick = ::exitApplication)
        },
        tooltip = APPLICATION_NAME,
        state = trayState
    )

    if (isApplicationLoading) {
        Window(
            onCloseRequest = ::exitApplication,
            resizable = false,
            alwaysOnTop = true,
            state = rememberWindowState(
                position = WindowPosition(Alignment.Center),
                width = 250.dp,
                height = 250.dp,
            ),
            undecorated = true,
            transparent = false,
            enabled = false,
        ) {
            Image(
                painter = painterResource("assets/splash.png"),
                modifier = Modifier.fillMaxSize(),
                contentDescription = "Splash"
            )
        }
        return@application
    }
    if (currentAuthentication == null) {
        Window(
            onCloseRequest = ::exitApplication,
            resizable = true,
            alwaysOnTop = false,
            state = rememberWindowState(
                position = WindowPosition(Alignment.Center),
                width = 640.dp,
                height = 480.dp,
            ),
            undecorated = false,
            transparent = false,
            enabled = true,
        ) {
            AuthView(
                appContext = appContext,
                onAuthSuccess = { user ->
                    currentAuthentication = Authentication(user);
                }
            )
        }
        return@application
    }

    Window(
        title = APPLICATION_NAME,
        onCloseRequest = ::exitApplication,
    ) {
        MainView(
            appContext = appContext,
            authentication = currentAuthentication!!
        )
    }
}