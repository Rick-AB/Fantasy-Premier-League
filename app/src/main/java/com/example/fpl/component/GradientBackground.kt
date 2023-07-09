package com.example.fpl.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.fpl.ui.theme.LightBlue
import com.example.fpl.ui.theme.SeaBlue

val horizontalGradientBrush = Brush.horizontalGradient(listOf(LightBlue, SeaBlue))
val verticalGradientBrush = Brush.verticalGradient(listOf(Color.Transparent, Color.White))

@Composable
fun GradientBackground(
    modifier: Modifier = Modifier,
    customVerticalBrush: Brush? = null,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(horizontalGradientBrush)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(customVerticalBrush ?: verticalGradientBrush)
        ) {
            content()
        }
    }
}
