package com.github.shaart.pstorage.multiplatform.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.shaart.pstorage.multiplatform.dto.PasswordViewDto
import com.github.shaart.pstorage.multiplatform.util.ClipboardUtil
import java.util.stream.IntStream

@Composable
fun PasswordsTable(passwords: List<PasswordViewDto>) {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        val columnState = rememberLazyListState()
        LazyColumn(
            state = columnState,

            ) {
            items(passwords) {
                Box(
                    modifier = Modifier.height(32.dp)
                        .fillMaxWidth()
                        .padding(start = 10.dp)
                ) {
                    Text(text = it.alias)
                }
            }
        }
        VerticalScrollbar(
            modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
            adapter = rememberScrollbarAdapter(scrollState = columnState)
        )
    }
}

@Preview
@Composable
fun preview() {
    PasswordsTable(
        passwords = IntStream.iterate(1) { it + 1 }
            .limit(100)
            .mapToObj { number ->
                PasswordViewDto(
                    alias = "alias$number",
                    copyValue = { ClipboardUtil.setValueToClipboard("$number") }
                )
            }.toList()
    )
}