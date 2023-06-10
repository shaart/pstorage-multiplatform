package com.github.shaart.pstorage.multiplatform.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.github.shaart.pstorage.multiplatform.dto.PasswordViewDto
import com.github.shaart.pstorage.multiplatform.exception.GlobalExceptionHandler
import com.github.shaart.pstorage.multiplatform.model.Authentication
import com.github.shaart.pstorage.multiplatform.preview.PreviewData
import com.github.shaart.pstorage.multiplatform.ui.table.TextTableCell

@Composable
fun PasswordsTable(
    authentication: Authentication,
    modifier: Modifier = Modifier.fillMaxWidth().fillMaxHeight(),
    globalExceptionHandler: GlobalExceptionHandler,
    onPasswordDelete: (PasswordViewDto) -> Unit,
    onPasswordCopy: (PasswordViewDto) -> Unit,
) {
    Box(
        modifier = modifier,
    ) {
        val columnState = rememberLazyListState()
        LazyColumn(
            state = columnState,
            modifier = Modifier.padding(8.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    TextTableCell(text = "Alias", weight = .3f, title = true)
                    TextTableCell(text = "Password", weight = .3f, title = true)
                    TextTableCell(text = "Actions", weight = .3f, title = true)
                }
                Divider(
                    color = Color.LightGray,
                    modifier = Modifier.height(1.dp).fillMaxHeight().fillMaxWidth()
                )
            }
            items(authentication.user.passwords) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(start = 10.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    TextTableCell(text = it.alias, weight = .35f)
                    TextTableCell(text = "***", weight = .35f)
                    Row(
                        modifier = Modifier.padding(start = 10.dp).weight(weight = .3f),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                    ) {
                        Button(
                            onClick = globalExceptionHandler.runSafely {
                                onPasswordCopy(it)
                            },
                        ) {
                            Text("Copy to clipboard")
                        }
                        Button(
                            onClick = globalExceptionHandler.runSafely {
                                onPasswordDelete(it)
                            }
                        ) {
                            Text("Delete")
                        }
                    }
                }
                Divider(
                    color = Color.LightGray,
                    modifier = Modifier.height(1.dp).fillMaxHeight().fillMaxWidth()
                )
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
fun previewPasswordsTable() {
    PasswordsTable(
        authentication = PreviewData.previewAuthentication(
            passwordsCount = 100
        ),
        globalExceptionHandler = PreviewData.previewGlobalExceptionHandler(),
        onPasswordCopy = {},
        onPasswordDelete = {},
    )
}