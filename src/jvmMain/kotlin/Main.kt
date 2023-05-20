import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Tray
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.github.shaart.pstorage.multiplatform.App
import kotlinx.coroutines.delay

private const val APPLICATION_NAME = "PStorage"

fun main() = application {
    var isApplicationLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(1000) // TODO database migrations, resources loading
        isApplicationLoading = false
    }

    Tray(
        icon = painterResource("assets/icons/tray/icon16.png"),
        menu = {
            Item("Quit App", onClick = ::exitApplication)
        },
        tooltip = APPLICATION_NAME,
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
    } else {
        Window(
            title = APPLICATION_NAME,
            onCloseRequest = ::exitApplication,
        ) {
            App()
        }
    }
}
