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
import com.github.shaart.pstorage.multiplatform.enums.AppSettings
import com.github.shaart.pstorage.multiplatform.model.Authentication
import com.github.shaart.pstorage.multiplatform.ui.ActiveViewContext
import com.github.shaart.pstorage.multiplatform.ui.Router
import com.github.shaart.pstorage.multiplatform.ui.ViewContextSnapshot
import com.github.shaart.pstorage.multiplatform.ui.Views
import kotlinx.coroutines.delay
import org.slf4j.LoggerFactory
import java.awt.Dimension
import java.util.*

class Main

fun main() = application {
    val log by remember { mutableStateOf(LoggerFactory.getLogger(Main::class.java)) }
    val appContext by remember { mutableStateOf(AppConfig.init(isMigrateDatabase = true)) }
    var isApplicationLoading by remember { mutableStateOf(true) }
    var isShowCurrentWindow by remember { mutableStateOf(true) }
    var isCurrentWindowAlwaysOnTop by remember { mutableStateOf(false) }
    val focusWindow = {
        isCurrentWindowAlwaysOnTop = true
        isCurrentWindowAlwaysOnTop = false
    }
    var currentAuthentication: Authentication? by remember { mutableStateOf(null) }
    var activeView by remember { mutableStateOf(ViewContextSnapshot(view = Views.AUTH)) }
    var activeViewHistory: Deque<ViewContextSnapshot> by remember { mutableStateOf(ArrayDeque()) }
    val activeViewContext = ActiveViewContext(
        getAuthentication = { currentAuthentication },
        setAuthentication = { currentAuthentication = it },
        changeView = { newActiveView ->
            if (Objects.equals(newActiveView, activeView)) {
                return@ActiveViewContext
            }
            activeViewHistory.push(activeView)
            activeView = newActiveView
            log.debug("ChangeView, history: {}", activeViewHistory)
        },
        goBack = {
            val prevView = activeViewHistory.poll()
            if (prevView != null) {
                activeView = prevView
            }
            log.debug("GoBack, history: {}", activeViewHistory)
        },
        clearHistory = {
            activeViewHistory = LinkedList()
            log.debug("ClearHistory, history: {}", activeViewHistory)
        }
    )
    val applicationNameWithVersion =
        "${appContext.properties().applicationName} (${appContext.properties().version.git.buildVersion})"
    val mainWindowState = rememberWindowState()

    LaunchedEffect(Unit) {
        delay(500)
        isApplicationLoading = false
        focusWindow()
    }
    val trayState = rememberTrayState()
    Tray(
        icon = painterResource("assets/icons/tray/icon16.png"),
        menu = {
            Item(
                text = applicationNameWithVersion,
                onClick = {
                    isShowCurrentWindow = true
                    activeViewContext.restoreActiveView()
                    activeViewContext.clearHistory()
                    focusWindow()
                }
            )
            Separator()
            Menu(text = "Passwords", enabled = currentAuthentication != null) {
                val passwords = currentAuthentication?.user?.passwords ?: emptyList()
                if (passwords.isEmpty()) {
                    Item(text = "No passwords found", enabled = false, onClick = {})
                } else {
                    passwords.forEach {
                        Item(
                            text = it.alias,
                            onClick = it.createCopyPasswordCommand(authentication = currentAuthentication!!)
                        )
                    }
                }
            }
            Separator()
            Item(
                text = "User settings",
                onClick = {
                    isShowCurrentWindow = true
                    activeViewContext.changeView(ViewContextSnapshot(view = Views.SETTINGS))
                    focusWindow()
                },
                enabled = currentAuthentication != null,
            )
            Item(
                text = "Log out...",
                onClick = {
                    activeViewContext.dropAuthentication()
                    focusWindow()
                },
                enabled = currentAuthentication != null,
            )
            Separator()
            Item(text = "Exit", onClick = ::exitApplication)
        },
        tooltip = applicationNameWithVersion,
        state = trayState,
        onAction = { isShowCurrentWindow = true },
    )

    val onCloseCommonWindow = {
        isShowCurrentWindow = false

        if (AppSettings.TRAY_NOTIFICATION_ON_CLOSE.isEnabled(currentAuthentication?.user)
                .toBoolean()
        ) {
            trayState.sendNotification(
                Notification(
                    title = applicationNameWithVersion,
                    message = "App is not closed, you can find it in tray",
                    type = Notification.Type.Info
                )
            )
        }
    }

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
    Window(
        title = applicationNameWithVersion,
        visible = isShowCurrentWindow,
        alwaysOnTop = isCurrentWindowAlwaysOnTop,
        onCloseRequest = onCloseCommonWindow,
        state = mainWindowState,
        icon = painterResource(appContext.properties().ui.taskbarIconPath),
    ) {
        window.minimumSize = Dimension(640, 480)
        Router(
            activeView = activeView,
            activeViewContext = activeViewContext,
            log = log,
            appContext = appContext,
        )
    }
}