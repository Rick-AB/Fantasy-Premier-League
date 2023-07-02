package com.example.fpl.model

class Coordinate(val x: Float, val y: Float) {
    companion object {
        val ZERO: Coordinate = Coordinate(0F, 0F)
    }
}

data class FieldBounds(
    val topLeftCoordinate: Coordinate,
    val topRightCoordinate: Coordinate,
    val bottomLeftCoordinate: Coordinate,
    val bottomRightCoordinate: Coordinate
)

class Field(availableWidth: Float, availableHeight: Float) {
    private var fieldTopWidth = 0F
    private var fieldBottomWidth = 0F
    private var fieldHeight = 0F

    var fieldBounds: FieldBounds = FieldBounds(
        Coordinate.ZERO,
        Coordinate.ZERO,
        Coordinate.ZERO,
        Coordinate.ZERO
    )
        private set


    var touchlineBounds: FieldBounds = FieldBounds(
        Coordinate.ZERO,
        Coordinate.ZERO,
        Coordinate.ZERO,
        Coordinate.ZERO
    )
        private set


    var eighteenYardBoxBounds: FieldBounds = FieldBounds(
        Coordinate.ZERO,
        Coordinate.ZERO,
        Coordinate.ZERO,
        Coordinate.ZERO
    )
        private set


    var sixYardBoxBounds: FieldBounds = FieldBounds(
        Coordinate.ZERO,
        Coordinate.ZERO,
        Coordinate.ZERO,
        Coordinate.ZERO
    )
        private set

    var kickOffLineBounds: FieldBounds = FieldBounds(
        Coordinate.ZERO,
        Coordinate.ZERO,
        Coordinate.ZERO,
        Coordinate.ZERO
    )
        private set

    var leftFlagStartCoordinate: Coordinate = Coordinate.ZERO
        private set
    var leftFlagEndCoordinate: Coordinate = Coordinate.ZERO
        private set
    var rightFlagStartCoordinate: Coordinate = Coordinate.ZERO
        private set
    var rightFlagEndCoordinate: Coordinate = Coordinate.ZERO

    init {
        calculateFieldBounds(availableWidth, availableHeight)
        calculateTouchlineBounds()
        calculate18YardBoxBounds()
        calculate6YardBoxBounds()
        calculateKickOffLineBounds()
        calculateCornerFlagBounds()
    }

    private fun calculateFieldBounds(maxWidth: Float, maxHeight: Float) {
        val topWidth = maxWidth.times(0.85F)
        val topSpacing = maxWidth.minus(topWidth).div(2)
        val topLeftX = 0F.plus(topSpacing)
        val topRightX = maxWidth.minus(topSpacing)
        val bottomWidth = maxWidth.times(1.5F)
        val bottomSpacing = bottomWidth.minus(maxWidth).div(2)
        val bottomLeftX = 0F.minus(bottomSpacing)
        val bottomRightX = maxWidth.plus(bottomSpacing)

        fieldTopWidth = topWidth
        fieldBottomWidth = bottomWidth
        fieldHeight = maxHeight

        val bottomLeft = Coordinate(bottomLeftX, maxHeight)
        val bottomRight = Coordinate(bottomRightX, maxHeight)
        val topLeft = Coordinate(topLeftX, 0F)
        val topRight = Coordinate(topRightX, 0F)


        fieldBounds = FieldBounds(
            topLeftCoordinate = topLeft,
            topRightCoordinate = topRight,
            bottomLeftCoordinate = bottomLeft,
            bottomRightCoordinate = bottomRight
        )
    }

    private fun calculateTouchlineBounds() {
        val widthShrinkFactor = 0.97F // 97% of field width
        val heightShrinkFactor = 0.95F // 95% of field height
        val topWidth = fieldTopWidth.times(widthShrinkFactor)
        val widthDiff = fieldTopWidth.minus(topWidth)
        val spacing = widthDiff.div(2)
        val topLeftX = fieldBounds.topLeftCoordinate.x.plus(spacing)
        val topRightX = fieldBounds.topRightCoordinate.x.minus(spacing)
        val bottomLeftX = fieldBounds.bottomLeftCoordinate.x.plus(spacing)
        val bottomRightX = fieldBounds.bottomRightCoordinate.x.minus(spacing)

        val touchlineHeight = fieldHeight.times(heightShrinkFactor)
        val heightSpacing = fieldHeight.minus(touchlineHeight).div(2)

        val bottomLeft = Coordinate(bottomLeftX, fieldHeight)
        val bottomRight = Coordinate(bottomRightX, fieldHeight)
        val topLeft = Coordinate(topLeftX, heightSpacing)
        val topRight = Coordinate(topRightX, heightSpacing)

        touchlineBounds = FieldBounds(
            topLeftCoordinate = topLeft,
            topRightCoordinate = topRight,
            bottomLeftCoordinate = bottomLeft,
            bottomRightCoordinate = bottomRight
        )
    }

    private fun calculate18YardBoxBounds() {
        val widthShrinkFactor = 0.65F // 65% of touchline width
        val heightShrinkFactor = 0.13F // 13% of touchline height
        val touchlineTopWidth =
            touchlineBounds.topRightCoordinate.x.minus(touchlineBounds.topLeftCoordinate.x)

        val eighteenYardWidth = touchlineTopWidth.times(widthShrinkFactor)
        val eighteenYardWidthDiff = touchlineTopWidth.minus(eighteenYardWidth)
        val spacing = eighteenYardWidthDiff.div(2)

        val topLeftX = touchlineBounds.topLeftCoordinate.x.plus(spacing)
        val topRightX = touchlineBounds.topRightCoordinate.x.minus(spacing)

        val eighteenYardHeight = fieldHeight.times(heightShrinkFactor)
        val bottomLeftX =
            calculateLeftXCoordinateForY(eighteenYardHeight) + spacing + 10F // add 10 pixels to reduce skewness
        val bottomRightX =
            calculateRightXCoordinateForY(eighteenYardHeight) - spacing - 10F // subtract 10 pixels to reduce skewness

        val bottomLeft = Coordinate(bottomLeftX, eighteenYardHeight)
        val bottomRight = Coordinate(bottomRightX, eighteenYardHeight)
        val topLeft = Coordinate(topLeftX, touchlineBounds.topLeftCoordinate.y)
        val topRight = Coordinate(topRightX, touchlineBounds.topRightCoordinate.y)

        eighteenYardBoxBounds = FieldBounds(
            topLeftCoordinate = topLeft,
            topRightCoordinate = topRight,
            bottomLeftCoordinate = bottomLeft,
            bottomRightCoordinate = bottomRight
        )
    }

    private fun calculate6YardBoxBounds() {
        val widthShrinkFactor = 0.3F // 30% of touchline width
        val heightShrinkFactor = 0.07F // 7% of field height
        val touchlineTopWidth =
            touchlineBounds.topRightCoordinate.x.minus(touchlineBounds.topLeftCoordinate.x)

        val sixYardWidth = touchlineTopWidth.times(widthShrinkFactor)
        val sixYardWidthDiff = touchlineTopWidth.minus(sixYardWidth)
        val widthSpacing = sixYardWidthDiff.div(2)

        val topLeftX = touchlineBounds.topLeftCoordinate.x.plus(widthSpacing)
        val topRightX = touchlineBounds.topRightCoordinate.x.minus(widthSpacing)

        val sixYardBoxHeight = fieldHeight.times(heightShrinkFactor)
        val bottomLeftX =
            calculateLeftXCoordinateForY(sixYardBoxHeight) + widthSpacing + 7.5F // 7.5 pixels reduce skewness
        val bottomRightX =
            calculateRightXCoordinateForY(sixYardBoxHeight) - widthSpacing - 7.5F // 7.5 pixels reduce skewness

        val bottomLeft = Coordinate(bottomLeftX, sixYardBoxHeight)
        val bottomRight = Coordinate(bottomRightX, sixYardBoxHeight)
        val topLeft = Coordinate(topLeftX, touchlineBounds.topLeftCoordinate.y)
        val topRight = Coordinate(topRightX, touchlineBounds.topRightCoordinate.y)

        sixYardBoxBounds = FieldBounds(
            topLeftCoordinate = topLeft,
            topRightCoordinate = topRight,
            bottomLeftCoordinate = bottomLeft,
            bottomRightCoordinate = bottomRight
        )
    }

    private fun calculateKickOffLineBounds() {
        val y = touchlineBounds.bottomLeftCoordinate.y * 0.5F
        val startX = calculateLeftXCoordinateForY(y)
        val endX = calculateRightXCoordinateForY(y)

        kickOffLineBounds = kickOffLineBounds.copy(
            topLeftCoordinate = Coordinate(startX, y),
            topRightCoordinate = Coordinate(endX, y),
        )
    }

    private fun calculateCornerFlagBounds() {
        val (topLeftCoordinate, topRightCoordinate, _, _) = touchlineBounds
        val y = topLeftCoordinate.y + 15F

        val leftFlagStartX = calculateLeftXCoordinateForY(y)
        val leftFlagEndX = topLeftCoordinate.x + 20F
        leftFlagStartCoordinate = Coordinate(leftFlagStartX, y)
        leftFlagEndCoordinate = Coordinate(leftFlagEndX, topLeftCoordinate.y)

        val rightFlagStartX = topRightCoordinate.x - 20F
        val rightFlagEndX = calculateRightXCoordinateForY(y)
        rightFlagStartCoordinate = Coordinate(rightFlagStartX, topRightCoordinate.y)
        rightFlagEndCoordinate = Coordinate(rightFlagEndX, y)
    }

    private fun calculateLeftXCoordinateForY(y: Float): Float {
        val y2 = touchlineBounds.bottomLeftCoordinate.y
        val y1 = touchlineBounds.topLeftCoordinate.y
        val x2 = touchlineBounds.bottomLeftCoordinate.x
        val x1 = touchlineBounds.topLeftCoordinate.x
        val slope = (y2 - y1) / (x2 - x1)

        return (y2 - y - (slope * x2)) / -(slope)
    }

    private fun calculateRightXCoordinateForY(y: Float): Float {
        val y2 = touchlineBounds.bottomRightCoordinate.y
        val y1 = touchlineBounds.topRightCoordinate.y
        val x2 = touchlineBounds.bottomRightCoordinate.x
        val x1 = touchlineBounds.topRightCoordinate.x
        val slope = (y2 - y1) / (x2 - x1)

        return (y2 - y - (slope * x2)) / -(slope)
    }
}