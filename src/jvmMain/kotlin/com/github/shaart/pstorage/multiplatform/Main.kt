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
import com.github.shaart.pstorage.multiplatform.model.Authentication
import com.github.shaart.pstorage.multiplatform.view.AuthView
import com.github.shaart.pstorage.multiplatform.view.MainView
import kotlinx.coroutines.delay
import java.awt.Dimension

fun main() = application {
    val appContext by remember { mutableStateOf(AppConfig.init(isMigrateDatabase = true)) }
    var isApplicationLoading by remember { mutableStateOf(true) }
    var currentAuthentication: Authentication? by remember { mutableStateOf(null) }
    var isShowCurrentWindow by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(500)
        isApplicationLoading = false
    }

    val trayState = rememberTrayState()
    Tray(
        icon = painterResource("assets/icons/tray/icon16.png"),
        menu = {
            Item(
                text = "${appContext.properties().applicationName} (${appContext.properties().applicationVersion})",
                onClick = { isShowCurrentWindow = true }
            )
            Separator()
            Menu(text = "Passwords") {
                currentAuthentication?.user?.passwords?.forEach {
                    Item(
                        text = it.alias,
                        onClick = it.copyPasswordCommand(authentication = currentAuthentication!!)
                    )
                }
            }
            Item(text = "Exit", onClick = ::exitApplication)
        },
        tooltip = appContext.properties().applicationName,
        state = trayState,
        onAction = { isShowCurrentWindow = true },
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
            title = appContext.properties().applicationName,
            visible = isShowCurrentWindow,
            onCloseRequest = { isShowCurrentWindow = false },
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
                    currentAuthentication = Authentication(user)
                }
            )
        }
        return@application
    }

    Window(
        title = appContext.properties().applicationName,
        visible = isShowCurrentWindow,
        onCloseRequest = { isShowCurrentWindow = false },
    ) {
        window.minimumSize = Dimension(640, 480)
        MainView(
            authentication = currentAuthentication!!
        )
    }
}