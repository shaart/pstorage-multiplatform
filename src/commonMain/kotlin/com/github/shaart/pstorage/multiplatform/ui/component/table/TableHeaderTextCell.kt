package com.github.shaart.pstorage.multiplatform.ui.component.table

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun RowScope.TableHeaderTextCell(
    text: String,
    weight: Float? = null,
    width: Dp? = null,
    alignment: TextAlign = TextAlign.Center,
    title: Boolean = false,
    useFillingWithSpacer: Boolean = false,
) {
    var modifier = Modifier.padding(0.dp)
    if (weight != null) {
        modifier = modifier.weight(weight = weight)
    }
    if (width != null) {
        modifier = modifier.width(width = width)
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier,
    ) {
        Text(
            text = text,
            fontWeight = if (title) FontWeight.Bold else FontWeight.Normal,
            textAlign = alignment,
        )
        if (useFillingWithSpacer) {
            Spacer(
                modifier = Modifier.height(1.dp).width(32.dp)
            )
        }
    }
}