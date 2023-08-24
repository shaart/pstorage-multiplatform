package com.github.shaart.pstorage.multiplatform.ui.component.navigation

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import com.github.shaart.pstorage.multiplatform.config.ApplicationContext
import com.github.shaart.pstorage.multiplatform.model.Authentication
import com.github.shaart.pstorage.multiplatform.preview.PreviewData
import com.github.shaart.pstorage.multiplatform.ui.model.navigation.ActiveViewContext
import com.github.shaart.pstorage.multiplatform.ui.model.navigation.ViewContextSnapshot
import com.github.shaart.pstorage.multiplatform.ui.model.navigation.Views
import com.github.shaart.pstorage.multiplatform.ui.view.AuthView
import com.github.shaart.pstorage.multiplatform.ui.view.MainView
import com.github.shaart.pstorage.multiplatform.ui.view.RegisterView
import com.github.shaart.pstorage.multiplatform.ui.view.SettingsView
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.slf4j.MDC

@Composable
fun Router(
    log: Logger,
    activeViewContext: ActiveViewContext,
    appContext: ApplicationContext,
    activeView: ViewContextSnapshot,
) {
    when (activeView.view) {
        Views.AUTH -> {
            activeViewContext.clearHistory()
            AuthView(
                appContext = appContext,
                onAuthSuccess = { user ->
                    activeViewContext.applyAuthentication(Authentication(user))
                    MDC.put("userId", user.id)
                    log.info("Successfully logged with userId = {}", user.id)
                    log.debug("Found settings: {}", user.settings)
                },
                onRegisterClick = {
                    activeViewContext.changeView(ViewContextSnapshot(view = Views.REGISTER))
                }
            )
        }

        Views.REGISTER -> RegisterView(
            appContext = appContext,
            activeViewContext = activeViewContext,
            onRegisterSuccess = { user ->
                activeViewContext.changeView(ViewContextSnapshot(view = Views.AUTH))
                log.info("Successfully registered userId = {}", user.id)
                log.debug("Found settings: {}", user.settings)
            }
        )

        else -> NavBar(activeViewContext = activeViewContext, activeView = activeView) {
            when (activeView.view) {
                Views.MAIN -> {
                    MainView(
                        authentication = activeViewContext.getAuthentication()!!,
                        passwordService = appContext.passwordService(),
                        onPasswordsChange = { newPasswords ->
                            activeViewContext.setAuthentication(
                                activeViewContext.getAuthentication()!!.withPasswords(newPasswords)
                            )
                        },
                        globalExceptionHandler = appContext.globalExceptionHandler(),
                    )
                }

                Views.SETTINGS -> SettingsView(
                    appContext = appContext,
                    activeViewContext = activeViewContext,
                    onSettingsChange = { newSettings ->
                        activeViewContext.setAuthentication(
                            activeViewContext.getAuthentication()!!.withSettings(newSettings)
                        )
                    }
                )

                else -> {
                    log.warn("Received unexpected view in Router: {}", activeView.view)
                }
            }
        }
    }
}

@Preview
@Composable
fun previewRouterMain() {
    Router(
        log = LoggerFactory.getLogger("Router"),
        activeViewContext = PreviewData.previewActiveViewContextUnauthorized(),
        appContext = PreviewData.previewApplicationContext(),
        activeView = ViewContextSnapshot(view = Views.MAIN),
    )
}

@Composable
fun previewRouter(activeView: ViewContextSnapshot) {
    Router(
        log = LoggerFactory.getLogger("Router"),
        activeViewContext = PreviewData.previewActiveViewContext(
            authentication = PreviewData.previewAuthentication(
                passwordsCount = 3
            )
        ),
        appContext = PreviewData.previewApplicationContext(),
        activeView = activeView,
    )
}