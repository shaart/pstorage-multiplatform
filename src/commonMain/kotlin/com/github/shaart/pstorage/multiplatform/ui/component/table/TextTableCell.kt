package com.github.shaart.pstorage.multiplatform.ui.component.table

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun RowScope.TextTableCell(
    text: String,
    weight: Float,
    alignment: TextAlign = TextAlign.Center,
    title: Boolean = false,
    editable: Boolean = false,
    shouldBeEmptyOnEditStart: Boolean = false,
    shouldBeMasked: Boolean = false,
    onEdit: (newValue: String) -> Unit = {},
) {
    var textValue by remember { mutableStateOf(text) }
    var isInEditionMode by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    if (editable && isInEditionMode) {
        Row(
            modifier = Modifier.weight(weight),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                enabled = true,
                modifier = Modifier.fillMaxWidth(.8f).focusRequester(focusRequester)
                    .focusProperties { },
                value = textValue,
                onValueChange = { textValue = it },
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.Black,
                    backgroundColor = Color.White,
                ),
                textStyle = TextStyle(
                    fontWeight = if (title) FontWeight.Bold else FontWeight.Normal,
                    textAlign = alignment,
                ),
                visualTransformation = if (shouldBeMasked) PasswordVisualTransformation() else VisualTransformation.None,
            )
            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }
            Row {
                IconButton(
                    modifier = Modifier.width(32.dp),
                    onClick = {
                        isInEditionMode = false
                        onEdit(textValue)
                    },
                ) {
                    Icon(imageVector = Icons.Filled.Check, contentDescription = "Confirm cell edit")
                }
                IconButton(
                    modifier = Modifier.width(32.dp),
                    onClick = {
                        isInEditionMode = false
                        textValue = text
                    },
                ) {
                    Icon(imageVector = Icons.Filled.Clear, contentDescription = "Cancel cell edit")
                }
            }
        }
    } else {
        Row(
            modifier = Modifier.weight(weight),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                modifier = Modifier.fillMaxWidth(.8f).padding(end = 10.dp),
                fontWeight = if (title) FontWeight.Bold else FontWeight.Normal,
                textAlign = alignment,
            )
            IconButton(
                modifier = Modifier.width(32.dp).fillMaxWidth(fraction = .2f),
                onClick = {
                    isInEditionMode = true
                    textValue = if (shouldBeEmptyOnEditStart) "" else text
                },
            ) {
                Icon(imageVector = Icons.Filled.Edit, contentDescription = "Edit cell value")
            }
        }
    }
}