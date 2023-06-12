package com.github.shaart.pstorage.multiplatform.ui

import androidx.compose.material.AlertDialog
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.github.shaart.pstorage.multiplatform.config.PstorageProperties
import com.github.shaart.pstorage.multiplatform.util.ExceptionUtil

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ErrorWindow(properties: PstorageProperties, e: Throwable) {
    AlertDialog(
        title = {
            Text("${properties.applicationName}: unexpected error")
        },
        text = {
            Text(ExceptionUtil.getStacktrace(e))
        },
        onDismissRequest = {},
        confirmButton = {},
    )
}