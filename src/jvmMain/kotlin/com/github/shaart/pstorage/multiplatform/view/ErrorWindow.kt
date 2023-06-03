package com.github.shaart.pstorage.multiplatform.view

import androidx.compose.material.AlertDialog
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.github.shaart.pstorage.multiplatform.APPLICATION_NAME
import com.github.shaart.pstorage.multiplatform.util.ExceptionUtil

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ErrorWindow(e: Throwable) {
    AlertDialog(
        title = {
            Text("$APPLICATION_NAME: unexpected error")
        },
        text = {
            Text(ExceptionUtil.getStacktrace(e))
        },
        onDismissRequest = {},
        confirmButton = {},
    )
}