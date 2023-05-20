import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Tray
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

private const val APPLICATION_NAME = "PStorage"

fun main() = application {
    val trayIcon = painterResource("assets/icons/tray/icon16.png")

    Tray(
        icon = trayIcon,
        menu = {
            Item("Quit App", onClick = ::exitApplication)
        },
        tooltip = APPLICATION_NAME,
    )

    Window(
        title = APPLICATION_NAME,
        onCloseRequest = ::exitApplication,
    ) {
        App()
    }
}
