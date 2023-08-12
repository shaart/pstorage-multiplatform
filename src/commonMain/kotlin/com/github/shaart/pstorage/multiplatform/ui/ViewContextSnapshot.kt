package com.github.shaart.pstorage.multiplatform.ui

data class ViewContextSnapshot(
    val view: Views,
    val viewParams: Map<String, String> = emptyMap(),
)