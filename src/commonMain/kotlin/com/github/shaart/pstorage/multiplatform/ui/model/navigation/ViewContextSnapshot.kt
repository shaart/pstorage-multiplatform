package com.github.shaart.pstorage.multiplatform.ui.model.navigation

data class ViewContextSnapshot(
    val view: Views,
    val viewParams: Map<String, String> = emptyMap(),
)