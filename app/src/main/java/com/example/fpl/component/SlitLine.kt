package com.example.fpl.component

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp

@Composable
fun SlitLine(modifier: Modifier, color: Color) {
    Canvas(modifier = modifier) {
        val width = this.size.width
        val path = Path().apply {
            moveTo(0f, 0f)
            cubicTo(
                x1 = 0.dp.toPx(),
                y1 = 0.dp.toPx(),
                x2 = width / 2,
                y2 = 1.dp.toPx(),
                x3 = width / 2,
                y3 = 1.dp.toPx()
            )
            cubicTo(
                x1 = width / 2,
                y1 = 1.dp.toPx(),
                x2 = width / 2,
                y2 = 1.dp.toPx(),
                x3 = width,
                y3 = 0.dp.toPx()
            )
        }

        drawPath(path, color)
    }
}