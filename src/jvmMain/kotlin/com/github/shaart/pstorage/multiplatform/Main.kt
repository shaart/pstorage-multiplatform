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
import com.github.shaart.pstorage.multiplatform.ui.AuthView
import com.github.shaart.pstorage.multiplatform.ui.MainView
import kotlinx.coroutines.delay
import org.slf4j.MDC
import java.awt.Dimension

fun main() = application {
    val log by remember { mutableStateOf(logger()) }
    val appContext by remember { mutableStateOf(AppConfig.init(isMigrateDatabase = true)) }
    var isApplicationLoading by remember { mutableStateOf(true) }
    var wasDisplayedOnCloseTrayNotification by remember { mutableStateOf(false) }
    var currentAuthentication: Authentication? by remember { mutableStateOf(null) }
    var isShowCurrentWindow by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(500)
        isApplicationLoading = false
    }

    val trayState = rememberTrayState()
    val applicationNameWithVersion =
        "${appContext.properties().applicationName} (${appContext.properties().version.git.buildVersion})"
    Tray(
        icon = painterResource("assets/icons/tray/icon16.png"),
        menu = {
            Item(
                text = applicationNameWithVersion,
                onClick = { isShowCurrentWindow = true }
            )
            Separator()
            Menu(text = "Passwords") {
                currentAuthentication?.user?.passwords?.forEach {
                    Item(
                        text = it.alias,
                        onClick = it.createCopyPasswordCommand(authentication = currentAuthentication!!)
                    )
                } ?: Item(text = "No passwords found", enabled = false, onClick = {})
            }
            Item(text = "Exit", onClick = ::exitApplication)
        },
        tooltip = applicationNameWithVersion,
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
            icon = painterResource(appContext.properties().ui.taskbarIconPath),
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
            title = applicationNameWithVersion,
            visible = isShowCurrentWindow,
            onCloseRequest = {
                isShowCurrentWindow = false

                if (!wasDisplayedOnCloseTrayNotification) {
                    trayState.sendNotification(
                        Notification(
                            title = applicationNameWithVersion,
                            message = "App is not closed, you can find it in tray",
                            type = Notification.Type.Info
                        )
                    )
                    wasDisplayedOnCloseTrayNotification = true
                }
            },
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
            icon = painterResource(appContext.properties().ui.taskbarIconPath),
        ) {
            AuthView(
                appContext = appContext,
                onAuthSuccess = { user ->
                    currentAuthentication = Authentication(user)
                    MDC.put("userId", user.id)
                    log.info("Successfully logged with userId = ${user.id}")
                }
            )
        }
        return@application
    }

    Window(
        title = applicationNameWithVersion,
        visible = isShowCurrentWindow,
        onCloseRequest = { isShowCurrentWindow = false },
        icon = painterResource(appContext.properties().ui.taskbarIconPath),
    ) {
        window.minimumSize = Dimension(640, 480)
        MainView(
            authentication = currentAuthentication!!,
            passwordService = appContext.passwordService(),
            onPasswordsChange = { newPasswords ->
                currentAuthentication = currentAuthentication?.withPasswords(newPasswords)
            },
            globalExceptionHandler = appContext.globalExceptionHandler(),
        )
    }
}