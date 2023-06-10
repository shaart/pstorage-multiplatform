package com.github.shaart.pstorage.multiplatform.ui.table

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
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
        TextField(
            enabled = true,
            modifier = Modifier.padding(10.dp).weight(weight).focusRequester(focusRequester)
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
        IconButton(onClick = {
            isInEditionMode = false
            onEdit(textValue)
        }) {
            Icon(imageVector = Icons.Filled.Check, contentDescription = "Confirm cell edit")
        }
        IconButton(onClick = {
            isInEditionMode = false
            textValue = text
        }) {
            Icon(imageVector = Icons.Filled.Clear, contentDescription = "Cancel cell edit")
        }
    } else {
        Text(
            text = text,
            modifier = Modifier.padding(10.dp).weight(weight),
            fontWeight = if (title) FontWeight.Bold else FontWeight.Normal,
            textAlign = alignment,
        )
        IconButton(onClick = {
            isInEditionMode = true
            textValue = if (shouldBeEmptyOnEditStart) "" else text
        }) {
            Icon(imageVector = Icons.Filled.Edit, contentDescription = "Edit cell value")
        }
    }
}