package com.example.fpl.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

@Composable
fun AutoResizeText(
    text: String,
    modifier: Modifier,
    color: Color = Color.Unspecified,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    reportUseableStyle: ((TextStyle) -> Unit)? = null
) {
    var resizeableTextStyle by remember(style) { mutableStateOf(style) }
    var shouldDraw by remember { mutableStateOf(false) }


    Text(
        text = text,
        softWrap = false,
        modifier = modifier.drawWithContent {
            if (shouldDraw) {
                drawContent()
            }
        },
        style = resizeableTextStyle,
        color = color,
        onTextLayout = { textLayoutResult ->
            if (textLayoutResult.didOverflowWidth) {
                resizeableTextStyle =
                    resizeableTextStyle.copy(fontSize = resizeableTextStyle.fontSize * 0.95)
            } else {
                shouldDraw = true
                if (reportUseableStyle != null) reportUseableStyle(resizeableTextStyle)
            }
        }
    )
}