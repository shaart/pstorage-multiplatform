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
import com.github.shaart.pstorage.multiplatform.dto.PasswordViewDto
import com.github.shaart.pstorage.multiplatform.model.Authentication
import com.github.shaart.pstorage.multiplatform.util.ClipboardUtil
import com.github.shaart.pstorage.multiplatform.view.AuthView
import com.github.shaart.pstorage.multiplatform.view.MainView
import kotlinx.coroutines.delay

val appContext: AppContext = AppConfig.init()

fun main() = application {
    var isApplicationLoading by remember { mutableStateOf(true) }
    var currentAuthentication: Authentication? by remember { mutableStateOf(null) }
    var isShowCurrentWindow by remember { mutableStateOf(true) }
    var passwords = mutableStateOf(
        listOf(
            PasswordViewDto(
                alias = "111",
                copyValue = { ClipboardUtil.setValueToClipboard("123") }),
            PasswordViewDto(
                alias = "222",
                copyValue = { ClipboardUtil.setValueToClipboard("345") }),
        )
    )

    LaunchedEffect(Unit) {
        delay(500) // TODO database migrations, resources loading
        isApplicationLoading = false
    }

    val trayState = rememberTrayState()
    Tray(
        icon = painterResource("assets/icons/tray/icon16.png"),
        menu = {
            Item(
                text = "${appContext.properties.applicationName} V${appContext.properties.applicationVersion}",
                onClick = { isShowCurrentWindow = true }
            )
            Separator()
            Menu(text = "Passwords") {
                passwords.value.forEach {
                    Item(text = it.alias, onClick = it.copyValue)
                }
            }
            Item(text = "Exit", onClick = ::exitApplication)
        },
        tooltip = appContext.properties.applicationName,
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
                    currentAuthentication = Authentication(user);
                }
            )
        }
        return@application
    }

    Window(
        title = appContext.properties.applicationName,
        visible = isShowCurrentWindow,
        onCloseRequest = { isShowCurrentWindow = false },
    ) {
        MainView(
            appContext = appContext,
            authentication = currentAuthentication!!
        )
    }
}