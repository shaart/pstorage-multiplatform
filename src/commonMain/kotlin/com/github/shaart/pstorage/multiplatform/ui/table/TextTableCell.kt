package com.github.shaart.pstorage.multiplatform.ui.table

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun RowScope.TextTableCell(
    text: String,
    weight: Float,
    alignment: TextAlign = TextAlign.Center,
    title: Boolean = false,
    editable: Boolean = false,
    onEdit: () -> Unit = {},
) {
    Button(
        onClick = onEdit,
        enabled = true,
        modifier = Modifier.fillMaxWidth()
            .shadow(0.dp)
            .weight(weight = weight)
            .clickable(enabled = editable, onClick = onEdit),
        elevation = ButtonDefaults.elevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp,
            hoveredElevation = 0.dp,
            focusedElevation = 0.dp
        ),
        contentPadding = PaddingValues(15.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.White,
            contentColor = Color.Black
        )
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = text,
                modifier = Modifier.padding(10.dp),
                fontWeight = if (title) FontWeight.Bold else FontWeight.Normal,
                textAlign = alignment,
            )
            if (editable) {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "Edit $text",
                    modifier = Modifier.align(Alignment.CenterEnd)
                )
            }
        }
    }
}