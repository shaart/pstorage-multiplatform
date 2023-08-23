package com.github.shaart.pstorage.multiplatform.ui.model.navigation

import com.github.shaart.pstorage.multiplatform.model.Authentication

data class ActiveViewContext(
    val getAuthentication: () -> Authentication?,
    val setAuthentication: (Authentication?) -> Unit,
    val changeView: (ViewContextSnapshot) -> Unit,
    val goBack: () -> Unit,
    val clearHistory: () -> Unit,
) {
    fun applyAuthentication(authentication: Authentication) {
        setAuthentication(authentication)
        changeView(ViewContextSnapshot(view = Views.MAIN))
        clearHistory()
    }

    fun dropAuthentication() {
        setAuthentication(null)
        changeView(ViewContextSnapshot(view = Views.AUTH))
        clearHistory()
    }

    fun restoreActiveView() {
        val targetView: Views = if (getAuthentication() == null) Views.AUTH else Views.MAIN
        changeView(ViewContextSnapshot(view = targetView))
    }
}