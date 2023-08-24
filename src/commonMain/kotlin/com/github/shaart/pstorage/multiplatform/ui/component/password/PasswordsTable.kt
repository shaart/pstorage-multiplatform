package com.github.shaart.pstorage.multiplatform.ui.component.password

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.github.shaart.pstorage.multiplatform.dto.PasswordViewDto
import com.github.shaart.pstorage.multiplatform.exception.GlobalExceptionHandler
import com.github.shaart.pstorage.multiplatform.model.Authentication
import com.github.shaart.pstorage.multiplatform.preview.PreviewData
import com.github.shaart.pstorage.multiplatform.ui.component.table.TableHeaderTextCell
import com.github.shaart.pstorage.multiplatform.ui.component.table.TextTableCell

@Composable
fun PasswordsTable(
    authentication: Authentication,
    modifier: Modifier = Modifier.fillMaxWidth().fillMaxHeight(),
    globalExceptionHandler: GlobalExceptionHandler,
    onPasswordDelete: (PasswordViewDto) -> Unit,
    onPasswordCopy: (PasswordViewDto) -> Unit,
    onPasswordEdit: (PasswordViewDto, String) -> Unit,
    onAliasEdit: (PasswordViewDto, String) -> Unit,
) {
    val rowButtonsModifier: Modifier by remember {
        derivedStateOf {
            Modifier.padding(start = 8.dp).height(38.dp)
        }
    }

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
                    modifier = Modifier.fillMaxWidth().fillMaxHeight().padding(vertical = 10.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    TableHeaderTextCell(text = "Alias", weight = .45f, title = true, useFillingWithSpacer = true)
                    TableHeaderTextCell(text = "Password", weight = .45f, title = true, useFillingWithSpacer = true)
                    TableHeaderTextCell(text = "Actions", width = 180.dp, title = true)
                }
                Divider(
                    color = Color.LightGray,
                    modifier = Modifier.height(1.dp).fillMaxHeight().fillMaxWidth()
                )
            }
            items(items = authentication.user.passwords) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    TextTableCell(
                        text = it.alias, weight = .45f, editable = true,
                        alignment = TextAlign.Left,
                        onEdit = { newValue ->
                            globalExceptionHandler.runSafely { onAliasEdit(it, newValue) }()
                        },
                    )
                    TextTableCell(
                        text = "**********", weight = .45f, editable = true,
                        shouldBeEmptyOnEditStart = true,
                        shouldBeMasked = true,
                        onEdit = { newValue ->
                            globalExceptionHandler.runSafely { onPasswordEdit(it, newValue) }()
                        },
                    )
                    Row(
                        modifier = Modifier.width(180.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = globalExceptionHandler.runSafely { onPasswordCopy(it) },
                            modifier = rowButtonsModifier,
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.ContentCopy,
                                contentDescription = "Copy password to clipboard",
                            )
                        }
                        Button(
                            onClick = globalExceptionHandler.runSafely { onPasswordDelete(it) },
                            modifier = rowButtonsModifier,
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Delete,
                                contentDescription = "Delete password",
                            )
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
        onPasswordEdit = { _, _ -> },
        onAliasEdit = { _, _ -> },
    )
}