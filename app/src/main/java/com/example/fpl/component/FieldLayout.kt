package com.example.fpl.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.ParentDataModifier
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import com.example.fpl.FieldLayoutScope
import com.example.fpl.model.Coordinate
import com.example.fpl.model.FieldBounds
import com.example.fpl.model.PlayerPosition
import com.example.fpl.ui.theme.DarkFieldGreen
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

@Composable
fun FieldLayout(
    modifier: Modifier,
    crossAxisSpacing: Dp,
    spacing: Dp,
    content: @Composable FieldLayoutScope.() -> Unit
) {
    Layout(
        modifier = modifier,
        content = { FieldLayoutScope.content() }
    ) { measurables, constraints ->
        val looseConstraints = constraints.copy(
            minWidth = 0,
            minHeight = 0,
        )

        val placeablesGroupedByPlayerPosition = measurables
            .map { measurable -> measurable.measure(looseConstraints) }
            .groupBy { (it.parentData as? PlayerData)?.position }

        val horizontalSpacingAsPx = spacing.toPx().roundToInt()
        val verticalSpacingAsPx = crossAxisSpacing.toPx().roundToInt()
        val height = placeablesGroupedByPlayerPosition
            .map { entry -> entry.value.maxOf { it.height } }
            .sum() + (verticalSpacingAsPx * (placeablesGroupedByPlayerPosition.size - 1))

        layout(constraints.maxWidth, height) {
            var y = 0

            placeablesGroupedByPlayerPosition.forEach { (position, placeables) ->
                check(placeables.size <= 5)

                if (position != null) {
                    val itemsWidth = placeables.sumOf { it.width }
                    val totalSpaceBetween = (placeables.size - 1) * horizontalSpacingAsPx
                    var x = (constraints.maxWidth - (itemsWidth + totalSpaceBetween)) / 2

                    placeables.forEach { placeable ->
                        placeable.placeRelative(x, y)
                        x += placeable.width + horizontalSpacingAsPx
                    }

                    y += verticalSpacingAsPx + placeables.maxOf { it.height }
                } else {
                    placeables.forEach { placeable ->
                        val availableHeight = constraints.maxHeight - y
                        val spaceBetweenPositionedItems = availableHeight - placeable.height
                        val newY =
                            if (spaceBetweenPositionedItems < verticalSpacingAsPx)
                                (constraints.maxHeight - placeable.height) +
                                        (abs(spaceBetweenPositionedItems) + verticalSpacingAsPx)
                            else
                                constraints.maxHeight - placeable.height

                        placeable.placeRelative(0, newY)
                    }
                }
            }
        }
    }
}

fun DrawScope.drawField(fieldBounds: FieldBounds) {
    val (topLeftCoordinate, topRightCoordinate, bottomLeftCoordinate, bottomRightCoordinate) = fieldBounds

    val path = Path().apply {
        reset()
        moveTo(topLeftCoordinate.x, topLeftCoordinate.y)
        lineTo(topRightCoordinate.x, topRightCoordinate.y)
        lineTo(bottomRightCoordinate.x, bottomRightCoordinate.y)
        lineTo(bottomLeftCoordinate.x, bottomLeftCoordinate.y)
        lineTo(topLeftCoordinate.x, topLeftCoordinate.y)
    }

    drawPath(
        path = path,
        color = DarkFieldGreen,
        style = Fill
    )
}

fun DrawScope.drawFieldLine(fieldBounds: FieldBounds) {
    val (topLeftCoordinate, topRightCoordinate, bottomLeftCoordinate, bottomRightCoordinate) = fieldBounds

    val path = Path().apply {
        reset()
        moveTo(topLeftCoordinate.x, topLeftCoordinate.y)
        lineTo(topRightCoordinate.x, topRightCoordinate.y)
        lineTo(bottomRightCoordinate.x, bottomRightCoordinate.y)
        lineTo(bottomLeftCoordinate.x, bottomLeftCoordinate.y)
        lineTo(topLeftCoordinate.x, topLeftCoordinate.y)
    }

    drawPath(
        path = path,
        color = Color.White,
        style = Stroke(3F)
    )
}

fun DrawScope.draw18YardArc(fieldBounds: FieldBounds) {
    val (_, topRightCoordinate, bottomLeftCoordinate, bottomRightCoordinate) = fieldBounds
    val curveRadius = (bottomRightCoordinate.y - topRightCoordinate.y) * 0.5F
    val eighteenYardBoxWidth = bottomRightCoordinate.x - bottomLeftCoordinate.x
    val widthPercentDiff = (eighteenYardBoxWidth - (eighteenYardBoxWidth * 0.4F)) / 2
    val x2 = bottomRightCoordinate.x - widthPercentDiff
    val x1 = bottomLeftCoordinate.x + widthPercentDiff
    val y2 = bottomRightCoordinate.y
    val y1 = bottomLeftCoordinate.y
    drawArc(x1, x2, y1, y2, curveRadius)
}

fun DrawScope.drawArc(x1: Float, x2: Float, y1: Float, y2: Float, radius: Float) {
    val midY = y1 + ((y2 - y1) / 2)
    val midX = x1 + ((x2 - x1) / 2)
    val xDiff = midX - x2
    val yDiff = midY - y2
    val angle = (atan2(yDiff, xDiff) * (180 / Math.PI)) - 90
    val angleRadians = Math.toRadians(angle)
    val pointX = (midX + radius * cos(angleRadians)).toFloat()
    val pointY = (midY + radius * sin(angleRadians)).toFloat()

    val path = Path().apply {
        moveTo(x1, y1)
        cubicTo(x1, y1, pointX, pointY, x2, y2)
    }

    drawPath(
        path = path,
        color = Color.White,
        style = Stroke(3F)
    )
}

fun DrawScope.drawCornerArc(startCoordinate: Coordinate, endCoordinate: Coordinate) {
    val radius = 10F
    drawArc(
        x1 = startCoordinate.x,
        x2 = endCoordinate.x,
        y1 = startCoordinate.y,
        y2 = endCoordinate.y,
        radius = radius
    )
}

fun DrawScope.drawKickOffLine(fieldBounds: FieldBounds) {
    val (topLeftCoordinate, topRightCoordinate, _, _) = fieldBounds
    val y = topRightCoordinate.y
    val x2 = topRightCoordinate.x
    val x1 = topLeftCoordinate.x
    val midX = (x2 - abs(x1)) / 2

    val linePath = Path().apply {
        val horizontalRadius = 170F
        val verticalRadius = 140F

        reset()
        moveTo(x1, y)
        lineTo(x2, y)

        // draw center circle
        moveTo(midX, y)
        arcTo(
            Rect(
                left = midX - horizontalRadius,
                top = y - verticalRadius,
                right = midX + horizontalRadius,
                bottom = y + verticalRadius
            ),
            startAngleDegrees = 0F,
            sweepAngleDegrees = 359F,
            forceMoveTo = false
        )
    }

    drawPath(
        path = linePath,
        color = Color.White,
        style = Stroke(6F)
    )

    val kickOffSpotPath = Path().apply {
        val horizontalRadius = 25F
        val verticalRadius = 20F

        reset()
        moveTo(midX, y)
        arcTo(
            Rect(
                left = midX - horizontalRadius,
                top = y - verticalRadius,
                right = midX + horizontalRadius,
                bottom = y + verticalRadius
            ),
            startAngleDegrees = 0F,
            sweepAngleDegrees = 359F,
            forceMoveTo = false
        )
    }

    drawPath(
        path = kickOffSpotPath,
        color = Color.White,
    )
}

data class PlayerData(val position: PlayerPosition) : ParentDataModifier {
    override fun Density.modifyParentData(parentData: Any?) = this@PlayerData

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        val otherPlayerData = other as? PlayerData ?: return false

        return position === otherPlayerData.position
    }

    override fun hashCode(): Int {
        return position.hashCode()
    }

    override fun toString(): String {
        return "PlayData(position=$position)"
    }

}