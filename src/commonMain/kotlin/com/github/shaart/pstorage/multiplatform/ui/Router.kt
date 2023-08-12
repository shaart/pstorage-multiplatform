package com.github.shaart.pstorage.multiplatform.ui

import androidx.compose.runtime.Composable
import com.github.shaart.pstorage.multiplatform.config.ApplicationContext
import com.github.shaart.pstorage.multiplatform.model.Authentication
import org.slf4j.Logger
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

        else -> NavBar(activeViewContext = activeViewContext) {
            when (activeView.view) {
                Views.REGISTER -> RegisterView(
                    appContext = appContext,
                    activeViewContext = activeViewContext,
                    onRegisterSuccess = { user ->
                        activeViewContext.changeView(ViewContextSnapshot(view = Views.AUTH))
                        log.info("Successfully registered userId = {}", user.id)
                        log.debug("Found settings: {}", user.settings)
                    }
                )

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